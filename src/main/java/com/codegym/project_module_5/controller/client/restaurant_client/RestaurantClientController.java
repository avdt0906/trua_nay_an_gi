package com.codegym.project_module_5.controller.client.restaurant_client;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.service.impl.restaurant_service_impl.DishService;
import com.codegym.project_module_5.service.impl.restaurant_service_impl.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/restaurant_client")
public class RestaurantClientController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private DishService dishService;

    @GetMapping("/{id}")
    public String showRestaurantDetails(@PathVariable Long id, Model model) {
        List<Dish> dishes = dishService.findByRestaurantId(id);
        Optional<Restaurant> restaurant = restaurantService.findById(id);
        model.addAttribute("restaurant", restaurant.get());
        model.addAttribute("dishes", dishes);
        return "client/restaurant_client";
    }
}
