package com.codegym.project_module_5.controller.home;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import com.codegym.project_module_5.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class HomeController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IDishService dishService;

    @GetMapping(value = {"/", "/home"})
    public String showHome(Model model,
                           @RequestParam(name = "search", required = false) String search,
                           @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            String username = authentication.getName();
            userService.findByUsername(username)
                    .ifPresent(user -> model.addAttribute("currentUser", user));
        }

        Page<Dish> dishPage;
        if (search != null && !search.trim().isEmpty()) {
            List<Dish> searched = (List<Dish>) dishService.searchAvailableDishesByName(search);
            dishPage = PageableExecutionUtils.getPage(searched, pageable, () -> searched.size());
        } else {
            dishPage = dishService.findAll(pageable);
        }

        List<Dish> dishes = dishPage.getContent();

        Map<Restaurant, List<Dish>> dishesByRestaurant =
                dishes.stream()
                        .filter(d -> d.getRestaurant() != null)
                        .collect(Collectors.groupingBy(
                                Dish::getRestaurant,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        model.addAttribute("dishesPage", dishPage);
        model.addAttribute("dishes", dishes);
        model.addAttribute("dishesByRestaurant", dishesByRestaurant);
        model.addAttribute("search", search);

        return "/homepage/index";
    }
}
