package com.codegym.project_module_5.controller.client;

import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.order_service.IOrderService;
import com.codegym.project_module_5.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
// SỬA 1: Thêm RequestMapping ở đây để tất cả các URL trong controller này đều bắt đầu bằng "/orders/history"
@RequestMapping("/orders/history")
public class OrderHistoryController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    // SỬA 2: Bây giờ, phương thức này sẽ xử lý URL GET "/orders/history"
    @GetMapping
    public String viewOrderHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/account/login";
        }

        String username = authentication.getName();
        User currentUser = userService.findByUsername(username).orElse(null);

        if (currentUser == null) {
            return "redirect:/account/login";
        }

        List<Orders> orderList = orderService.findOrdersByUser(currentUser);
        model.addAttribute("orderList", orderList);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAuthenticated", true);

        return "user/order_history";
    }

    // SỬA 3: Phương thức này giờ sẽ xử lý URL GET "/orders/history/detail/{orderId}", khớp với liên kết trong HTML
    @GetMapping("/detail/{orderId}")
    public String orderDetail(@PathVariable Long orderId, Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/account/login";
        }

        Optional<Orders> orderOptional = orderService.findById(orderId);
        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();
            // Kiểm tra bảo mật để đảm bảo người dùng chỉ xem được đơn hàng của chính mình
            if (!order.getUser().getUsername().equals(authentication.getName())) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền truy cập đơn hàng này.");
                return "redirect:/orders/history";
            }
            model.addAttribute("order", order);
            return "user/order_detail";
        }
        redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng.");
        return "redirect:/orders/history";
    }

    // SỬA 4: Phương thức này giờ sẽ xử lý URL POST "/orders/history/cancel/{id}"
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/account/login";
        }

        User currentUser = userService.findByUsername(authentication.getName()).orElse(null);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Không thể xác thực người dùng.");
            return "redirect:/orders/history";
        }

        try {
            orderService.cancelOrder(id, currentUser);
            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng thành công.");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi! Không thể hủy đơn hàng.");
        }

        // SỬA 5: Chuyển hướng về trang chi tiết để người dùng thấy trạng thái đã thay đổi
        return "redirect:/orders/history/detail/" + id;
    }

    @PostMapping("/update_status/{id}")
    public String updateStatusOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/account/login";
        }

        User currentUser = userService.findByUsername(authentication.getName()).orElse(null);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Không thể xác thực người dùng.");
            return "redirect:/orders/history";
        }

        if (orderService.findById(id).get().getOrderStatus().getId() != 4) {
            redirectAttributes.addFlashAttribute("error", "Lỗi! Không thể nhận đơn hàng.");
        } else {
            try {
                orderService.updateOrderStatus(id);
                redirectAttributes.addFlashAttribute("success", "Đã nhận đơn thành công.");
            } catch (SecurityException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi! Không thể nhận đơn hàng.");
            }
        }
        // SỬA 5: Chuyển hướng về trang chi tiết để người dùng thấy trạng thái đã thay đổi
        return "redirect:/orders/history/detail/" + id;
    }
}
