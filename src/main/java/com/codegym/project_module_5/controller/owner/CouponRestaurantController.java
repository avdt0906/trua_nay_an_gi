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
        ModelAndView mv = new ModelAndView("redirect:/restaurants/coupons/coupon_list");
        return mv;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
//        Coupon coupon = couponService.findById(id);
//        if (coupon == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(coupon);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Coupon coupon) {
//        Coupon updated = couponService.update(id, coupon);
//        if (updated == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(updated);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
//        couponService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }
}

