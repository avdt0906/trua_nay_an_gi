package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.Restaurant;
import com.codegym.project_module_5.model.dto.request.RestaurantRegisterRequest;
import com.codegym.project_module_5.service.IRestaurantService;
import com.codegym.project_module_5.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private IRestaurantService restaurantService;
    
    @Autowired
    private IUserService userService;

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
            return "owner/restaurant/register_restaurant";
        }

        try {
            String currentUsername = principal != null ? principal.getName() : null;

            restaurantService.registerRestaurant(request, currentUsername);

            model.addAttribute("successMessage", "Đăng ký nhà hàng thành công!");
            return "owner/restaurant/dashboard";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "owner/restaurant/register_restaurant";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đăng ký thất bại: " + e.getMessage());
            return "owner/restaurant/register_restaurant";
        }
    }

}
