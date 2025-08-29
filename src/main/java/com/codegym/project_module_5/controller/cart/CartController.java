package com.codegym.project_module_5.controller.cart;

import com.codegym.project_module_5.model.cart_model.CartItem;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.model.user_model.UserAddress;
import com.codegym.project_module_5.service.cart_service.ICartService;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import com.codegym.project_module_5.service.order_service.IOrderService;
import com.codegym.project_module_5.service.order_service.IOrderDetailService;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.model.order_model.OrderStatus;
import com.codegym.project_module_5.repository.order_repository.IOrderStatusRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private IRestaurantService restaurantService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private IOrderStatusRepository orderStatusRepository;

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
            try {
                cartService.addToCart(currentUser, dish, quantity);
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        } else {
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }
            if (!cart.isEmpty()) {
                Long anyDishId = cart.keySet().iterator().next();
                Optional<Dish> anyDishOpt = dishService.findById(anyDishId);
                if (anyDishOpt.isPresent()) {
                    Long existingRestaurantId = anyDishOpt.get().getRestaurant().getId();
                    Long newRestaurantId = dish.getRestaurant().getId();
                    if (!existingRestaurantId.equals(newRestaurantId)) {
                        return ResponseEntity.badRequest().body("Chỉ được đặt món từ một nhà hàng trong mỗi đơn");
                    }
                }
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
            List<CartItem> cartItemList = cartService.getCartItems(currentUser);
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

            Object paymentMethod = session.getAttribute("paymentMethod");
            Object orderNote = session.getAttribute("orderNote");
            if (paymentMethod != null) {
                model.addAttribute("paymentMethod", paymentMethod.toString());
            }
            if (orderNote != null) {
                model.addAttribute("orderNote", orderNote.toString());
            }

            double subtotal = totalPrice;
            String appliedCoupon = (String) session.getAttribute("appliedCoupon");
            double discount = 0;
            String couponMessage = null;
            if (appliedCoupon != null && !appliedCoupon.isBlank() && !cartItemList.isEmpty()) {
                Long restaurantId = cartItemList.get(0).getDish().getRestaurant().getId();
                try {
                    var coupons = restaurantService.getCouponsByRestaurantId(restaurantId);
                    var matched = coupons.stream()
                            .filter(c -> Boolean.TRUE.equals(c.getIsAvailable()))
                            .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(appliedCoupon.trim()))
                            .findFirst();
                    if (matched.isPresent()) {
                        var c = matched.get();
                        boolean minOk = c.getMinOrder() == null || subtotal >= c.getMinOrder();
                        if (minOk) {
                            double d = 0;
                            if (c.getFixedDiscount() != null) d += c.getFixedDiscount();
                            if (c.getPercentDiscount() != null) d += subtotal * (c.getPercentDiscount() / 100.0);
                            if (c.getMaxDiscount() != null) d = Math.min(d, c.getMaxDiscount());
                            discount = Math.max(0, Math.min(d, subtotal));
                            couponMessage = "Đã áp dụng mã: " + c.getName();
                        } else {
                            couponMessage = "Đơn tối thiểu chưa đạt để áp dụng mã.";
                        }
                    } else {
                        couponMessage = "Mã giảm giá không hợp lệ.";
                    }
                } catch (Exception e) {
                    couponMessage = "Không thể áp dụng mã lúc này.";
                }
            }

            double serviceFee = Math.round(subtotal * 0.05);
            double shippingFee = 15000;
            double grandTotal = Math.max(0, subtotal - discount) + serviceFee + shippingFee;

            model.addAttribute("appliedCoupon", appliedCoupon);
            model.addAttribute("couponMessage", couponMessage);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("discount", discount);
            model.addAttribute("serviceFee", serviceFee);
            model.addAttribute("shippingFee", shippingFee);
            model.addAttribute("grandTotal", grandTotal);
        }

        return "cart/cart_detail";
    }


    @PostMapping("/checkout")
    public String submitCheckout(@RequestParam(name = "paymentMethod", required = false) String paymentMethod,
                                 @RequestParam(name = "note", required = false) String note,
                                 HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        if (!isAuthenticated) {
            return "redirect:/login";
        }

        if (paymentMethod != null && (paymentMethod.equals("COD") || paymentMethod.equals("CARD"))) {
            session.setAttribute("paymentMethod", paymentMethod);
        } else {
            session.removeAttribute("paymentMethod");
        }

        if (note != null) {
            session.setAttribute("orderNote", note.trim());
        } else {
            session.removeAttribute("orderNote");
        }

        return "redirect:/cart/detail";
    }

    @PostMapping("/place-order")
    public String placeOrder(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        if (!isAuthenticated) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User currentUser = userService.findByUsername(username).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItemList = cartService.getCartItems(currentUser);
        if (cartItemList == null || cartItemList.isEmpty()) {
            return "redirect:/cart";
        }

        Long restaurantId = cartItemList.get(0).getDish().getRestaurant().getId();
        var restaurantOpt = restaurantService.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            return "redirect:/cart/detail";
        }

        OrderStatus status = orderStatusRepository.findByName("PENDING")
                .orElseGet(() -> {
                    OrderStatus s = new OrderStatus();
                    s.setName("PENDING");
                    return orderStatusRepository.save(s);
                });

        Orders order = new Orders();
        order.setUser(currentUser);
        order.setRestaurant(restaurantOpt.get());
        order.setOrderStatus(status);
        Object note = session.getAttribute("orderNote");
        if (note != null) order.setCustomerNote(note.toString());
        orderService.save(order);

        for (CartItem ci : cartItemList) {
            OrderDetail od = new OrderDetail();
            od.setOrder(order);
            od.setDish(ci.getDish());
            od.setQuantity((long) ci.getQuantity());
            orderDetailService.save(od);
        }

        cartService.clearCart(currentUser);
        session.removeAttribute("paymentMethod");
        session.removeAttribute("orderNote");
        session.removeAttribute("appliedCoupon");

        return "redirect:/cart";
    }

    @PostMapping("/apply-coupon")
    @ResponseBody
    public ResponseEntity<?> applyCoupon(@RequestParam("couponCode") String couponCode, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        if (!isAuthenticated) {
            return ResponseEntity.status(401).build();
        }
        if (couponCode == null || couponCode.trim().isEmpty()) {
            session.removeAttribute("appliedCoupon");
        } else {
            session.setAttribute("appliedCoupon", couponCode.trim());
        }
        return ResponseEntity.ok().build();
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
