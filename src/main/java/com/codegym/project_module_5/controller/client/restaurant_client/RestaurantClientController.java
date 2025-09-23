package com.codegym.project_module_5.controller.client.restaurant_client;

import com.codegym.project_module_5.model.evaluate_model.Evaluate;
import com.codegym.project_module_5.model.restaurant_model.Category;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.repository.restaurant_repository.ICategoryRepository;
import com.codegym.project_module_5.service.impl.evaluate_service_impl.EvaluateService;
import com.codegym.project_module_5.service.impl.restaurant_service_impl.CategoryService;
import com.codegym.project_module_5.service.impl.restaurant_service_impl.DishService;
import com.codegym.project_module_5.service.impl.restaurant_service_impl.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/restaurant_client")
public class RestaurantClientController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EvaluateService evaluateService;

    @GetMapping("/{id}")
    public String showRestaurantDetails(@PathVariable Long id, Model model) {
        List<Dish> dishes = dishService.findByRestaurantId(id);
        Optional<Restaurant> restaurant = restaurantService.findById(id);
        Iterable<Category> categories = categoryService.findAll();
        Iterable<Evaluate> evaluates = evaluateService.findAll();
        Dish bannerDish = null;
    if (restaurant.isPresent() && restaurant.get().getFeaturedDishId() != null) {
        Long featuredId = restaurant.get().getFeaturedDishId();
        bannerDish = dishService.findById(featuredId).orElse(null);
    }
    model.addAttribute("bannerDish", bannerDish);
       
        if (!dishes.isEmpty()) {
            double minPrice = dishes.stream().mapToDouble(Dish::getPrice).min().orElse(0);
            double maxPrice = dishes.stream().mapToDouble(Dish::getPrice).max().orElse(0);
            String priceRange = String.format("%.0fđ - %.0fđ", minPrice, maxPrice);
            model.addAttribute("priceRange", priceRange);
        } else {
            model.addAttribute("priceRange", "Đang cập nhật");
        }

        model.addAttribute("evaluate", evaluates);
        model.addAttribute("restaurant", restaurant.get());
        model.addAttribute("categories", categories);
        model.addAttribute("dishes", dishes);
        return "client/restaurant_client";
    }
}
