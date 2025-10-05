package com.codegym.project_module_5.controller.owner;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codegym.project_module_5.model.restaurant_model.Restaurant;

import com.codegym.project_module_5.service.order_service.IOrderService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;

@Controller
@RequestMapping("/partner")
public class PartnerController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IRestaurantService restaurantService;

     private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Optional<Restaurant> restaurant = restaurantService.findByUsername(getCurrentUsername());
        model.addAttribute("restaurant", restaurant.get());
        return "owner/restaurant/partner_dashboard";

    }
    @PostMapping("/revenue")
    public String checkRevenue(@RequestParam Long restaurantId, RedirectAttributes redirectAttributes) {
        Double revenue = restaurantService.getMonthlyRevenue(restaurantId);
        redirectAttributes.addFlashAttribute("monthlyRevenue", revenue);
        redirectAttributes.addFlashAttribute("canRegister", revenue > 100_000_000);
        return "redirect:/partner/dashboard";
    }

    @PostMapping("/register")
    public String registerPartner(@RequestParam Long restaurantId, RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService.findById(restaurantId).orElseThrow();
        Double revenue = restaurantService.getMonthlyRevenue(restaurantId);

        if (revenue > 100_000_000) {
            restaurant.setPartnerRequest(true);
            restaurantService.save(restaurant);
            redirectAttributes.addFlashAttribute("success", "Gửi yêu cầu đối tác thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Doanh thu chưa đủ 100 triệu/tháng.");
        }

        return "redirect:/partner/dashboard";
    }
    
}
