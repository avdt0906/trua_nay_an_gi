package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.Restaurant;
import com.codegym.project_module_5.service.IRestaurantService;
import com.codegym.project_module_5.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    @Autowired
    private IRestaurantService restaurantService;

    @Autowired
    private IUserService userService;

    @GetMapping("/update_restaurant_form")
    public ModelAndView showUpdateRestaurantForm() {
        ModelAndView mv = new ModelAndView("owner/restaurant/update_restaurant_form");
        String username = getCurrentUsername();
        Optional<Restaurant> restaurant = restaurantService.findByUsername(username);
        mv.addObject("restaurant", restaurant.get());
        return mv;
    }

    @PostMapping("/update_restaurant")
    public ModelAndView updateRestaurant(@ModelAttribute("restaurant") Restaurant restaurant) {
        restaurantService.save(restaurant);
        ModelAndView mv = new ModelAndView("redirect:/restaurants/update_restaurant_form");
        return mv;
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return username;
        }

        return null;
    }
}
