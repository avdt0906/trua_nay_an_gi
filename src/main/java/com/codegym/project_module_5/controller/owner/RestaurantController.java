package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.dto.request.RestaurantRegisterRequest;
import com.codegym.project_module_5.service.IRestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private IRestaurantService restaurantService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("restaurant", new RestaurantRegisterRequest());
        return "owner/restaurant/register_restaurant";
    }

    @PostMapping("/signup")
    public String registerRestaurant(
            @Valid @ModelAttribute("restaurant") RestaurantRegisterRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model) {

        if (bindingResult.hasErrors()) {
            // Quan trọng: phải truyền lại object restaurant để hiển thị validation errors
            model.addAttribute("restaurant", request);
            return "owner/restaurant/register_restaurant";
        }

        try {
            String currentUsername = principal != null ? principal.getName() : null;

            restaurantService.registerRestaurant(request, currentUsername);

            return "redirect:/?success=restaurant_registered";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "owner/restaurant/register_restaurant";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đăng ký thất bại: " + e.getMessage());
            return "owner/restaurant/register_restaurant";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        return "owner/restaurant/dashboard";
    }
}
