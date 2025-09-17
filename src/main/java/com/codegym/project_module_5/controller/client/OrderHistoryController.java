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

import java.util.List;

@Controller
public class OrderHistoryController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @GetMapping("/orders/history")
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
}
