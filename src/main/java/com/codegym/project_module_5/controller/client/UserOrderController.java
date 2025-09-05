package com.codegym.project_module_5.controller.client;

import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.order_service.IOrderDetailService;
import com.codegym.project_module_5.service.order_service.IOrderService;
import com.codegym.project_module_5.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/my-orders")
public class UserOrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderDetailService orderDetailService;

    // THÊM PHƯƠNG THỨC NÀY
    // Phương thức này sẽ được gọi trước tất cả các request trong controller
    // Nó cung cấp các biến chung cho model, giải quyết lỗi
    @ModelAttribute
    public void commonUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            Optional<User> currentUser = userService.findByUsername(username);
            currentUser.ifPresent(user -> model.addAttribute("currentUser", user));
            model.addAttribute("isAuthenticated", true);
        } else {
            model.addAttribute("isAuthenticated", false);
        }
    }

    // LẤY currentUser TỪ MODEL THAY VÌ TÌM LẠI
    @GetMapping
    public String listOrders(Model model, @ModelAttribute("currentUser") User currentUser) {
        if (currentUser == null) {
            return "redirect:/account/login"; // Yêu cầu đăng nhập nếu chưa có
        }

        Iterable<Orders> orders = orderService.findByUserId(currentUser.getId());

        Map<Long, Double> orderTotals = new HashMap<>();
        for (Orders order : orders) {
            Iterable<OrderDetail> details = orderDetailService.findAllByOrderId(order.getId());
            double total = 0;
            for (OrderDetail detail : details) {
                total += detail.getDish().getPrice() * detail.getQuantity();
            }
            orderTotals.put(order.getId(), total);
        }

        model.addAttribute("orders", orders);
        model.addAttribute("orderTotals", orderTotals);
        // không cần add "currentUser" nữa vì @ModelAttribute đã làm

        return "user/order_list";
    }

    // LẤY currentUser TỪ MODEL THAY VÌ TÌM LẠI
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable("id") Long orderId, RedirectAttributes redirectAttributes, @ModelAttribute("currentUser") User currentUser) {
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để thực hiện thao tác.");
            return "redirect:/account/login";
        }

        Orders order = orderService.findById(orderId).orElse(null);

        if (order == null || !order.getUser().getId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đơn hàng không hợp lệ hoặc bạn không có quyền hủy đơn hàng này.");
            return "redirect:/my-orders";
        }

        boolean isCancelled = orderService.cancelOrder(orderId);
        if (isCancelled) {
            redirectAttributes.addFlashAttribute("successMessage", "Đã hủy đơn hàng #" + orderId + " thành công.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể hủy đơn hàng này. Đơn hàng có thể đã được xử lý.");
        }
        return "redirect:/my-orders";
    }
}

