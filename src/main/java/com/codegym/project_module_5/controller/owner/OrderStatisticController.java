package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.service.impl.order_service_impl.OrderDetailService;
import com.codegym.project_module_5.service.impl.order_service_impl.OrderService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order_statistic")
public class OrderStatisticController {

    @Autowired
    private OrderDetailService orderDetailService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private IRestaurantService restaurantService;

    @GetMapping
    @PreAuthorize("hasAuthority('OWNER')")
    public String showOrderStatistics(Model model) {
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            return "redirect:/account/login";
        }
        
        Optional<Restaurant> restaurantOpt = restaurantService.findByUsername(currentUsername);
        if (restaurantOpt.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy thông tin nhà hàng");
            return "/owner/order/order_statistic";
        }
        
        Restaurant restaurant = restaurantOpt.get();
        Long restaurantId = restaurant.getId();
        
        Iterable<Orders> orders = orderService.findAllByRestaurantId(restaurantId);
        
        List<OrderDetail> orderDetailList = new java.util.ArrayList<>();
        for (Orders order : orders) {
            Iterable<OrderDetail> details = orderDetailService.findAllByOrderId(order.getId());
            for (OrderDetail detail : details) {
                orderDetailList.add(detail);
            }
        }
        
        long totalOrders = ((java.util.Collection<?>) orders).size();
        long totalItems = orderDetailList.stream().mapToLong(OrderDetail::getQuantity).sum();
        double totalRevenue = orderDetailList.stream()
                .mapToDouble(detail -> detail.getQuantity() * detail.getDish().getPrice())
                .sum();
        
        model.addAttribute("orderDetailList", orderDetailList);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalRevenue", totalRevenue);
        
        return "/owner/order/order_statistic";
    }
    
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }
}
