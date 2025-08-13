package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.Category;
import com.codegym.project_module_5.model.Dish;
import com.codegym.project_module_5.model.Restaurant;
import com.codegym.project_module_5.repository.ICategoryRepository;
import com.codegym.project_module_5.service.IDishService;
import com.codegym.project_module_5.service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ModelAndView dishList() {
        ModelAndView mv = new ModelAndView("owner/dish/dish_list");
        String username = getCurrentUsername();
        Optional<Restaurant> restaurant = restaurantService.findByUsername(username);
        Iterable<Dish> dishes = dishService.findAllByRestaurant(restaurant.get().getId());
        mv.addObject("dishes", dishes);
        return mv;
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
        ModelAndView mv = new ModelAndView("redirect:/restaurants/dishes/add_dish_form");
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

    @GetMapping("/update_dish_form")
    public ModelAndView showUpdateDishForm() {
        ModelAndView mv = new ModelAndView("owner/dish/update_dish_form");
        Iterable<Category> categories = categoryRepository.findAll();
        mv.addObject("categories", categories);

        mv.addObject("dish", new Dish());
        return mv;
    }

}
