package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.restaurant_model.Category;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.repository.restaurant_repository.ICategoryRepository;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/restaurants/dishes")
public class DishRestaurantController {
    @Autowired
    IDishService dishService;

    @Autowired
    IRestaurantService restaurantService;

    @Autowired
    ICategoryRepository categoryRepository;


    @GetMapping("/dish_list")
    public ModelAndView dishList(@RequestParam(value = "search", required = false) String search,@PageableDefault(size = 2) Pageable pageable) {
        ModelAndView mv = new ModelAndView("owner/dish/dish_list");
        String username = getCurrentUsername();
        Optional<Restaurant> restaurantOptional = restaurantService.findByUsername(username);

        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            Page<Dish> dishes;
            if (search != null && !search.isEmpty()) {
                dishes = dishService.findAllByRestaurantIdAndNameContainingIgnoreCase(restaurant.getId(), search, pageable);
            } else {
                dishes = dishService.findAllByRestaurantId(restaurant.getId(), pageable);
            }
            mv.addObject("dishes", dishes);
            mv.addObject("restaurant", restaurant);
            mv.addObject("search", search);
            return mv;
        } else {
            // Nếu chủ quán chưa có nhà hàng, chuyển hướng đến trang đăng ký.
            return new ModelAndView("redirect:/restaurants/signup");
        }
    }

    @GetMapping("/add_dish_form")
    public ModelAndView showAddDishForm() {
        ModelAndView mv = new ModelAndView("owner/dish/add_dish_form");
        Iterable<Category> categories = categoryRepository.findAll();
        mv.addObject("categories", categories);

        mv.addObject("dish", new Dish());
        return mv;
    }

    @PostMapping("/add_dish")
    public ModelAndView addDish(Dish dish) {
        String username = getCurrentUsername();
        Optional<Restaurant> restaurant = restaurantService.findByUsername(username);
        dish.setRestaurant(restaurant.get());
        dishService.save(dish);
        ModelAndView mv = new ModelAndView("redirect:/restaurants/dishes/dish_list");
        return mv;
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }

    @GetMapping("/edit_dish_form/{id}")
    public ModelAndView showEditDishForm(@PathVariable("id") Long id) {
        Optional<Dish> dishOptional = dishService.findById(id);
        if (dishOptional.isPresent()) {
            Dish dish = dishOptional.get();
            ModelAndView mv = new ModelAndView("owner/dish/edit_dish_form");
            Iterable<Category> categories = categoryRepository.findAll();
            mv.addObject("categories", categories);
            mv.addObject("dish", dish);
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("redirect:/restaurants/dishes/dish_list");
            return mv;
        }
    }

    @PostMapping("/edit_dish")
    public ModelAndView editDish(@ModelAttribute("dish") Dish dish){
        dishService.save(dish);
        ModelAndView mv = new ModelAndView("redirect:/restaurants/dishes/dish_list");
        return mv;
    }

    @GetMapping("/delete_dish/{id}")
    public ModelAndView deleteDish(@PathVariable("id") Long id) {
        dishService.delete(id);
        ModelAndView mv = new ModelAndView("redirect:/restaurants/dishes/dish_list");
        return mv;
    }

}
