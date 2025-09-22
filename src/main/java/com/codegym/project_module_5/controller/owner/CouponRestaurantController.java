package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.restaurant_model.Coupon;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.service.restaurant_service.ICouponService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/restaurants/coupons")
public class CouponRestaurantController {
    @Autowired
    private ICouponService couponService;

    @Autowired
    IRestaurantService restaurantService;

    @GetMapping("/coupon_list")
    public ModelAndView couponList(@PageableDefault(size = 10) Pageable pageable) {
        ModelAndView mv = new ModelAndView("owner/coupon/coupon_list");
        Optional<Restaurant> restaurantOptional = restaurantService.findByUsername(getCurrentUsername());

        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            Page<Coupon> coupons = couponService.findAllByRestaurantId(pageable, restaurant.getId());
            mv.addObject("coupons", coupons);
            mv.addObject("restaurant", restaurant);
            mv.addObject("activePage", "");
            return mv;
        } else {
            // Nếu chủ quán chưa có nhà hàng, chuyển hướng đến trang đăng ký.
            return new ModelAndView("redirect:/restaurants/signup");
        }
    }

    @GetMapping("/add_coupon_form")
    public ModelAndView showAddCouponForm() {
        ModelAndView mv = new ModelAndView("owner/coupon/add_coupon_form");
        mv.addObject("coupon", new Coupon());
        return mv;
    }

    @PostMapping("/add_coupon")
    public ModelAndView addCoupon(@ModelAttribute("coupon") Coupon coupon){
        String username = getCurrentUsername();
        Optional<Restaurant> restaurant = restaurantService.findByUsername(username);
        coupon.setRestaurant(restaurant.get());
        couponService.save(coupon);
        return new ModelAndView("redirect:/restaurants/coupons/coupon_list");
    }

    @GetMapping("/edit_coupon_form/{id}")
    public ModelAndView showEditCouponForm(@PathVariable("id") Long id) {
        Optional<Coupon> coupon = couponService.findById(id);
        ModelAndView mv = new ModelAndView("owner/coupon/edit_coupon_form");
        mv.addObject("coupon",coupon.get());
        return mv;
    }

    @PostMapping("/edit_coupon")
    public ModelAndView editCoupon(@ModelAttribute("coupon") Coupon coupon){
        couponService.save(coupon);
        return new ModelAndView("redirect:/restaurants/coupons/coupon_list");

    }

    @GetMapping("/delete_coupon/{id}")
    public ModelAndView deleteCoupon(@PathVariable("id") Long id) {
        couponService.delete(id);
        return new ModelAndView("redirect:/restaurants/coupons/coupon_list");
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }
}

