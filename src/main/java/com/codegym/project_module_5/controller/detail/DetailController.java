package com.codegym.project_module_5.controller.detail;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Coupon;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.service.impl.dish_detail_service_impl.DishDetailService;
import com.codegym.project_module_5.service.restaurant_service.ICategoryService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

/*
* lấy danh sách vourcher từ nhà hàng
* lấy chi tiết món từ danh mục
* lấy món ăn phổ biến theo nhà hàng
* */
@Controller
@RequestMapping("/dish")
public class DetailController {
    
    @Autowired
    private DishDetailService dishDetailService;
    
    @Autowired
    private IRestaurantService restaurantService;

    @GetMapping("/{id}")
    public String showDishDetail(@PathVariable Long id, Model model) {
        
        Optional<Dish> dishOptional = dishDetailService.findById(id);
        
        if (dishOptional.isPresent()) {
            Dish dish = dishOptional.get();
            
            Restaurant restaurant = dish.getRestaurant();

            List<Coupon> coupons = restaurantService.getCouponsByRestaurantId(restaurant.getId());

            List<Dish> similarDishes = dishDetailService.findSimilarDishesByCategory(dish.getCategory().getId(), dish.getId());

            List<Dish> popularDishes = dishDetailService.findPopularDishesByRestaurant(restaurant.getId(), dish.getId());
            
            model.addAttribute("dish", dish);
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("coupons", coupons);
            model.addAttribute("similarDishes", similarDishes);
            model.addAttribute("popularDishes", popularDishes);
            
            return "detail/dish_detail";
        }
        
        return "redirect:/home";
    }
}
