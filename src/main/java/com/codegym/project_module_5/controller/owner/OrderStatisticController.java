package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.impl.order_service_impl.OrderDetailService;
import com.codegym.project_module_5.service.impl.order_service_impl.OrderService;
import com.codegym.project_module_5.service.impl.restaurant_service_impl.DishService;
import com.codegym.project_module_5.service.impl.restaurant_service_impl.RestaurantService;
import com.codegym.project_module_5.service.impl.user_service_impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/restaurants/orders")
public class OrderStatisticController {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private DishService dishService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserService userService;


    @GetMapping("/dish/{dishId}")
    public ModelAndView showOrderStatisticByDish(@PathVariable("dishId") Long dishId) {
        List<OrderDetail> orderDetailList = orderDetailService.findByDishId(dishId);
        ModelAndView mv = new ModelAndView("/owner/order/order_statistic");
        mv.addObject("orderDetailList", orderDetailList);
        Optional<Dish> dishOpt = dishService.findById(dishId);
        dishOpt.ifPresent(d -> mv.addObject("dishName", d.getName()));

        int sum = 0;
        for (OrderDetail orderDetail : orderDetailList) {
            sum += orderDetail.getQuantity();
        }
        mv.addObject("sum", sum);
        return mv;
    }

    @GetMapping("/list")
    public ModelAndView showOrderStatisticByRestaurant() {
        ModelAndView mv = new ModelAndView("owner/order/order_by_restaurant");
        String username = getCurrentUsername();
        Optional<User> user = userService.findByUsername(username);
        Optional<Restaurant> restaurantOptional = restaurantService.findRestaurantIdByUserId(user.get().getId());
        Iterable<Orders> orders = orderService.findAllByRestaurantId(restaurantOptional.get().getId());
        mv.addObject("ordersList", orders);
        return mv;
    }

    @GetMapping("/update/{orderId}")
    public ModelAndView updateOrderStatus(@PathVariable("orderId") Long orderId) {
        ModelAndView mv = new ModelAndView("redirect:/restaurants/orders/list");
        String username = getCurrentUsername();
        Optional<User> user = userService.findByUsername(username);
        Optional<Restaurant> restaurantOptional = restaurantService.findRestaurantIdByUserId(user.get().getId());
        Optional<Orders> ordersOptional = orderService.findById(orderId);
        if (ordersOptional.get().getRestaurant().getId() == restaurantOptional.get().getId()){
            orderService.updateOrderStatus(orderId);
        }
        return mv;
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }
}
