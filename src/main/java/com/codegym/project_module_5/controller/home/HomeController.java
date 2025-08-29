package com.codegym.project_module_5.controller.home;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import com.codegym.project_module_5.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("")
public class HomeController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IDishService dishService;

    @GetMapping(value = {"/", "/home"})
    public String showhome(Model model, @RequestParam(name = "search", required = false) String search) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            String username = authentication.getName();
            Optional<User> userOptional = userService.findByUsername(username);
            userOptional.ifPresent(user -> model.addAttribute("currentUser", user));
        }

        // Lấy danh sách món ăn
        List<Dish> dishes;
        if (search != null && !search.trim().isEmpty()) {
            dishes = (List<Dish>) dishService.searchAvailableDishesByName(search);
        } else {
            dishes = (List<Dish>) dishService.findAllAvailableDishes();
        }

        // Nhóm theo nhà hàng
        Map<Restaurant, List<Dish>> dishesByRestaurant = new LinkedHashMap<>();
        for (Dish dish : dishes) {
            Restaurant restaurant = dish.getRestaurant();
            if (restaurant != null) {
                dishesByRestaurant.computeIfAbsent(restaurant, k -> new ArrayList<>()).add(dish);
            }
        }

        model.addAttribute("dishes", dishes); // ✅ add this
        model.addAttribute("dishesByRestaurant", dishesByRestaurant);
        model.addAttribute("search", search);

        return "/homepage/index";
    }

}
