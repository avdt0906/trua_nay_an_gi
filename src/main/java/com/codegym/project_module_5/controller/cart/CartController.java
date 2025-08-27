package com.codegym.project_module_5.controller.cart;

import com.codegym.project_module_5.model.cart_model.CartItem;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.user_model.User;
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
    @ResponseBody // Thêm ResponseBody để trả về dữ liệu thay vì view
    public ResponseEntity<?> removeCartItem(@PathVariable("itemId") Long itemId, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);

        try {
            if (isAuthenticated) {
                // Người dùng đã đăng nhập: Xóa trong DB
                cartService.removeCartItem(itemId);
            } else {
                // Khách: Xóa trong Session
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
            // Xử lý cho người dùng đã đăng nhập
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            cartService.addToCart(currentUser, dish, quantity);
        } else {
            // Xử lý cho khách (dùng session)
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }
            cart.put(dishId, cart.getOrDefault(dishId, 0) + quantity);
            session.setAttribute("cart", cart);
        }

        return ResponseEntity.ok("Thêm vào giỏ hàng thành công!");
    }


    @GetMapping("/detail")
    public String showCartDetail(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        
        if (!isAuthenticated) {
            return "redirect:/login";
        }
        
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username).orElse(null);
        
        if (currentUser != null) {
            // Sử dụng cùng logic như method viewCart để đảm bảo dữ liệu nhất quán
            List<CartItem> cartItemList = cartService.getCartItems(currentUser);
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("currentUser", currentUser);
            
            // Tính tổng tiền và tổng số lượng
            double totalPrice = 0;
            int totalQuantity = 0;
            if (cartItemList != null) {
                for (CartItem item : cartItemList) {
                    totalPrice += item.getDish().getPrice() * item.getQuantity();
                    totalQuantity += item.getQuantity();
                }
            }
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalQuantity", totalQuantity);
        }
        
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "cart/cart_detail";
    }

    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Integer> getCartCount(HttpSession session) {
        // ... giữ nguyên logic
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
