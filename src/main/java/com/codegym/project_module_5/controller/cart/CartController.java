package com.codegym.project_module_5.controller.cart;

import com.codegym.project_module_5.model.cart_model.CartItem;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.model.user_model.UserAddress;
import com.codegym.project_module_5.service.cart_service.ICartService;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import com.codegym.project_module_5.service.user_service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDishService dishService;

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        double totalPrice = 0;
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username).orElse(null);
            model.addAttribute("currentUser", currentUser);
            if (currentUser != null) {
                List<CartItem> cartItems = cartService.getCartItems(currentUser);
                model.addAttribute("cartItems", cartItems);
                for (CartItem item : cartItems) {
                    totalPrice += item.getDish().getPrice() * item.getQuantity();
                }
            }
        } else {
            Map<Long, Integer> sessionCart = (Map<Long, Integer>) session.getAttribute("cart");
            if (sessionCart != null && !sessionCart.isEmpty()) {
                List<CartItem> cartItems = new ArrayList<>();
                for (Map.Entry<Long, Integer> entry : sessionCart.entrySet()) {
                    Optional<Dish> dishOptional = dishService.findById(entry.getKey());
                    if (dishOptional.isPresent()) {
                        Dish dish = dishOptional.get();
                        CartItem item = new CartItem(dish.getId(), null, dish, entry.getValue());
                        cartItems.add(item);
                        totalPrice += dish.getPrice() * entry.getValue();
                    }
                }
                model.addAttribute("cartItems", cartItems);
            }
        }
        model.addAttribute("totalPrice", totalPrice);
        return "cart/cart";
    }

    @PostMapping("/remove/{itemId}")
    @ResponseBody
    public ResponseEntity<?> removeCartItem(@PathVariable("itemId") Long itemId, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);

        try {
            if (isAuthenticated) {
                cartService.removeCartItem(itemId);
            } else {
                Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
                if (cart != null) {
                    cart.remove(itemId);
                    session.setAttribute("cart", cart);
                }
            }
            return ResponseEntity.ok().body(Map.of("message", "Xóa thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lỗi khi xóa sản phẩm."));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam("dishId") Long dishId,
                                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                                            HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);

        Optional<Dish> dishOptional = dishService.findById(dishId);
        if (dishOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
        }
        Dish dish = dishOptional.get();

        if (isAuthenticated) {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            cartService.addToCart(currentUser, dish, quantity);
        } else {
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }
            cart.put(dishId, cart.getOrDefault(dishId, 0) + quantity);
            session.setAttribute("cart", cart);
        }

        return ResponseEntity.ok("Thêm vào giỏ hàng thành công!");
    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam(value = "selectedItems", required = false) List<Long> selectedDishIds, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);

        if (isAuthenticated) {
            if (selectedDishIds == null || selectedDishIds.isEmpty()) {
                return "redirect:/cart";
            }
            String params = selectedDishIds.stream()
                    .map(id -> "selectedItems=" + id)
                    .collect(Collectors.joining("&"));
            return "redirect:/cart/detail?" + params;
        } else {
            if (selectedDishIds != null && !selectedDishIds.isEmpty()) {
                session.setAttribute("selectedDishIds", selectedDishIds);
            }
            session.setAttribute("redirectAfterLogin", "/cart/detail");
            return "redirect:/account/login";
        }
    }

    @GetMapping("/detail")
    public String showCartDetail(Model model, HttpSession session, @RequestParam(value = "selectedItems", required = false) List<Long> selectedDishIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/account/login";
        }

        if (selectedDishIds == null || selectedDishIds.isEmpty()) {
            return "redirect:/cart";
        }

        String username = authentication.getName();
        User currentUser = userService.findByUsername(username).orElse(null);

        if (currentUser != null) {
            List<CartItem> allCartItems = cartService.getCartItems(currentUser);

            List<CartItem> selectedCartItems = allCartItems.stream()
                    .filter(item -> selectedDishIds.contains(item.getDish().getId()))
                    .collect(Collectors.toList());

            model.addAttribute("cartItemList", selectedCartItems);
            model.addAttribute("currentUser", currentUser);

            double totalPrice = 0;
            int totalQuantity = 0;
            for (CartItem item : selectedCartItems) {
                if (item.getDish() != null) {
                    totalPrice += item.getDish().getPrice() * item.getQuantity();
                    totalQuantity += item.getQuantity();
                }
            }
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalQuantity", totalQuantity);

            List<UserAddress> addressList = userService.getUserAddresses(currentUser.getId());
            model.addAttribute("addressList", addressList);
            model.addAttribute("newAddress", new UserAddress());
        }

        return "cart/cart_detail";
    }

    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Integer> getCartCount(HttpSession session) {
        int count = 0;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        if (isAuthenticated) {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username).orElse(null);
            if (currentUser != null) {
                count = cartService.getCartItems(currentUser).size();
            }
        } else {
            Map<Long, Integer> sessionCart = (Map<Long, Integer>) session.getAttribute("cart");
            if (sessionCart != null) {
                count = sessionCart.size();
            }
        }
        return ResponseEntity.ok(count);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateCartItem(@RequestParam("itemId") Long itemId, @RequestParam("quantity") int quantity, HttpSession session) {
        return ResponseEntity.ok().build();
    }
}
