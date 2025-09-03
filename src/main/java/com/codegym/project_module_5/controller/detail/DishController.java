package com.codegym.project_module_5.controller.detail;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Coupon;
import com.codegym.project_module_5.model.restaurant_model.DishOption;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.service.impl.dish_option_impl.DishOptionService;
import com.codegym.project_module_5.service.impl.restaurant_service_impl.DishService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/*
* lấy danh sách vourcher từ nhà hàng
* lấy chi tiết món từ danh mục
* lấy món ăn phổ biến theo nhà hàng
* */
@Controller
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishOptionService dishOptionService;
    
    @Autowired
    private DishService dishService;
    
    @Autowired
    private IRestaurantService restaurantService;

    @GetMapping("/{id}")
    public String showDishDetail(@PathVariable Long id, Model model) {
        
        Optional<Dish> dishOptional = dishService.findById(id);
        
        if (dishOptional.isPresent()) {
            Dish dish = dishOptional.get();
            
            Restaurant restaurant = dish.getRestaurant();

            List<Coupon> coupons = restaurantService.getCouponsByRestaurantId(restaurant.getId());

            List<Dish> similarDishes = dishService.findSimilarDishesByCategory(dish.getCategory().getId(), dish.getId());

            List<Dish> popularDishes = dishService.findPopularDishesByRestaurant(restaurant.getId(), dish.getId());

            List<DishOption> dishOptionsList = dishOptionService.findDishOptionByDishId(id);

            Collections.shuffle(similarDishes);
            int limit = Math.min(similarDishes.size(), 4);
            similarDishes = similarDishes.subList(0, limit);

            model.addAttribute("dish", dish);
            model.addAttribute("dishOptions", dishOptionsList);
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("coupons", coupons);
            model.addAttribute("similarDishes", similarDishes);
            model.addAttribute("popularDishes", popularDishes);
            
            return "detail/dish_detail";
        }
        
        return "redirect:/home";
    }
}
