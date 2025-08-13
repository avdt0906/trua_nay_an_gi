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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public String showDishList(Model model, @RequestParam(value = "search", required = false) String search) {
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
            return "owner/dish/list";
        } else {
            // Nếu chủ quán chưa có nhà hàng, chuyển hướng đến trang đăng ký.
            return "redirect:/restaurants/signup";
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
        ModelAndView mv = new ModelAndView("redirect:/restaurants/dishes");
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

}
