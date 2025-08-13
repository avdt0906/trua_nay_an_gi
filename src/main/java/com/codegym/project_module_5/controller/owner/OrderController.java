package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.service.order_service.IOrderDetailService;
import com.codegym.project_module_5.service.order_service.IOrderService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/restaurants/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IRestaurantService restaurantService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @GetMapping
    public String showOrderList(Model model) {
        String username = getCurrentUsername();
        Optional<Restaurant> restaurantOptional = restaurantService.findByUsername(username);

        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            Iterable<Orders> orders = orderService.findAllByRestaurantId(restaurant.getId());
            model.addAttribute("orders", orders);
            return "owner/order/list";
        }
        return "redirect:/restaurants/signup";
    }

    @GetMapping("/{id}")
    public String showOrderDetail(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Orders> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();
            Iterable<OrderDetail> orderDetails = orderDetailService.findAllByOrderId(id);

            // Tính tổng tiền cho đơn hàng
            double totalPrice = 0;
            for (OrderDetail detail : orderDetails) {
                totalPrice += detail.getDish().getPrice() * detail.getQuantity();
            }

            model.addAttribute("order", order);
            model.addAttribute("orderDetails", orderDetails);
            model.addAttribute("totalPrice", totalPrice);
            return "owner/order/detail";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đơn hàng.");
        return "redirect:/restaurants/orders";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boolean isCancelled = orderService.cancelOrder(id);
        if (isCancelled) {
            redirectAttributes.addFlashAttribute("successMessage", "Đã hủy đơn hàng thành công.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể hủy đơn hàng này (có thể do trạng thái không hợp lệ).");
        }
        return "redirect:/restaurants/orders";
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }
}
