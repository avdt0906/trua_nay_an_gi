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

import java.util.Optional;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private IRestaurantService restaurantService;
    
    @Autowired
    private IUserService userService;
    
    @GetMapping("/signup")
    public String showRestaurantRegisterForm(Model model) {
        String currentUsername = getCurrentUsername();
        Optional<Restaurant> existingRestaurant = restaurantService.findByUsername(currentUsername);
        
        if (existingRestaurant.isPresent()) {
            model.addAttribute("errorMessage", "Bạn đã có nhà hàng rồi!");
            return "redirect:/restaurants/dashboard";
        }
        
        model.addAttribute("restaurantRegisterRequest", new RestaurantRegisterRequest());
        return "owner/restaurant/register_restaurant";
    }


    @PostMapping("/signup")
    public String registerRestaurant(@Valid @ModelAttribute("restaurantRegisterRequest") RestaurantRegisterRequest request,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "owner/restaurant/register_restaurant";
        }
        
        try {
            String currentUsername = getCurrentUsername();
            Restaurant restaurant = restaurantService.registerRestaurant(request, currentUsername);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Đăng ký nhà hàng thành công! Nhà hàng của bạn đang chờ admin phê duyệt.");
            return "redirect:/restaurants/dashboard";
            
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "owner/restaurant/register_restaurant";
        }
    }
    
    @GetMapping("/dashboard")
    public String showRestaurantDashboard(Model model) {
        String currentUsername = getCurrentUsername();
        Optional<Restaurant> restaurant = restaurantService.findByUsername(currentUsername);
        
        if (restaurant.isPresent()) {
            model.addAttribute("restaurant", restaurant.get());
            return "owner/restaurant/dashboard";
        } else {
            return "redirect:/restaurants/register";
        }
    }
    
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        
        return null;
    }
}
