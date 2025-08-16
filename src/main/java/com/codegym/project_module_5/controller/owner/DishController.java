package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.restaurant_model.Category;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.repository.restaurant_repository.ICategoryRepository;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/restaurants/dishes")
public class DishController {
    @Autowired
    IDishService dishService;

    @Autowired
    IRestaurantService restaurantService;

    @Autowired
    ICategoryRepository categoryRepository;


    @GetMapping("/dish_list")
    public ModelAndView dishList(Model model, @RequestParam(value = "search", required = false) String search) {
        ModelAndView mv = new ModelAndView("owner/dish/dish_list");
        String username = getCurrentUsername();
        Optional<Restaurant> restaurantOptional = restaurantService.findByUsername(username);

        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            Iterable<Dish> dishes;
            if (search != null && !search.isEmpty()) {
                dishes = dishService.findAllByRestaurantIdAndNameContainingIgnoreCase(restaurant.getId(), search);
            } else {
                dishes = dishService.findAllByRestaurantId(restaurant.getId());
            }
            model.addAttribute("dishes", dishes);
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("search", search); // Để giữ lại từ khóa tìm kiếm trên ô input
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

    @GetMapping("/update_dish_form")
    public ModelAndView showUpdateDishForm() {
        ModelAndView mv = new ModelAndView("owner/dish/update_dish_form");
        Iterable<Category> categories = categoryRepository.findAll();
        mv.addObject("categories", categories);

        mv.addObject("dish", new Dish());
        return mv;
    }

    @GetMapping("/delete_dish/{id}")
    public ModelAndView deleteDish(@PathVariable("id") Long id) {
        dishService.delete(id);
        ModelAndView mv = new ModelAndView("redirect:/restaurants/dishes/dish_list");
        return mv;
    }

}
