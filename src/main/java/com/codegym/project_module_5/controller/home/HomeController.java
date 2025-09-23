package com.codegym.project_module_5.controller.home;

import com.codegym.project_module_5.model.baner_model.Banner;
import com.codegym.project_module_5.model.restaurant_model.Category;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.service.Banner.IBannerService;
import com.codegym.project_module_5.service.restaurant_service.ICategoryService;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import com.codegym.project_module_5.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class HomeController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IDishService dishService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IBannerService bannerService;

    @Autowired
    private IRestaurantService restaurantService;

    @GetMapping
    public String index(Model model,
                        @PageableDefault(value = 8) Pageable pageable,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "categoryId", required = false) Long categoryId,
                        @RequestParam(name = "menuType", required = false, defaultValue = "default") String menuType,
                        @RequestParam(name = "couponCode", required = false) String couponCode) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("isAuthenticated", isAuthenticated);

        Page<Dish> dishPage;
        if (couponCode != null && !couponCode.isEmpty()) {
            Page<Restaurant> restaurantPage = restaurantService.findByCouponCodeAndIsAcceptedTrue(pageable, couponCode);
            List<Dish> dishesFromRestaurants = new ArrayList<>();
            for (Restaurant restaurant : restaurantPage.getContent()) {
                // SỬA LẠI TÊN PHƯƠNG THỨC CHO ĐÚNG
                dishesFromRestaurants.addAll(dishService.findByRestaurantId(restaurant.getId()));
            }
            // Tạo một trang mới từ danh sách các món ăn đã thu thập
            dishPage = new PageImpl<>(dishesFromRestaurants, pageable, dishesFromRestaurants.size());
            model.addAttribute("couponCode", couponCode);
        }
        else if (search != null && !search.isEmpty()) {
            // Sửa lại theo phương thức mới trong IDishService
            dishPage = dishService.search(search, pageable);
        } else if (categoryId != null) {
            // Sửa lại theo phương thức mới trong IDishService
            dishPage = dishService.findByCategoryIdAndRestaurantApproved(categoryId, pageable);
        } else if (menuType != null && !menuType.equals("default")) {
            List<Dish> menuDishes;
            switch (menuType) {
                case "new-dishes":
                    // Giả sử có phương thức này, nếu không cần tạo mới
                    // Dựa theo IDishService, không có phương thức này
                    menuDishes = new ArrayList<>(); // Cần implementation cụ thể
                    break;
                case "most-ordered":
                    // Giả sử có phương thức này, nếu không cần tạo mới
                    // Dựa theo IDishService, không có phương thức này
                    menuDishes = new ArrayList<>(); // Cần implementation cụ thể
                    break;
                case "best-sellers":
                    // Giả sử có phương thức này, nếu không cần tạo mới
                    // Dựa theo IDishService, không có phương thức này
                    menuDishes = new ArrayList<>(); // Cần implementation cụ thể
                    break;
                default:
                    menuDishes = new ArrayList<>();
                    dishService.findAllAvailableDishes().forEach(menuDishes::add);
                    break;
            }
            dishPage = PageableExecutionUtils.getPage(menuDishes, pageable, () -> menuDishes.size());
        } else {
            dishPage = dishService.findAll(pageable);
        }

        List<Dish> dishes = dishPage.getContent();

        Map<Restaurant, List<Dish>> dishesByRestaurant = dishes.stream()
                .filter(d -> d.getRestaurant() != null)
                .collect(Collectors.groupingBy(
                        Dish::getRestaurant,
                        LinkedHashMap::new,
                        Collectors.toList()));

        Iterable<Category> categories = categoryService.findAll();
        List<Banner> featured = bannerService.getFeaturedBanners();
        List<Banner> promotion = bannerService.getPromotionBanners();
        model.addAttribute("featuredBanners", featured);
        model.addAttribute("promotionBanners", promotion);
        model.addAttribute("dishesPage", dishPage);
        model.addAttribute("dishes", dishes);
        model.addAttribute("dishesByRestaurant", dishesByRestaurant);
        model.addAttribute("search", search);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("menuType", menuType);
        model.addAttribute("categories", categories);

        return "/homepage/index";
    }
}