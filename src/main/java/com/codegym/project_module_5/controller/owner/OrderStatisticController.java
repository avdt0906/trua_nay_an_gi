package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.service.impl.order_service_impl.OrderDetailService;
import com.codegym.project_module_5.service.impl.order_service_impl.OrderService;
import com.codegym.project_module_5.service.impl.restaurant_service_impl.DishService;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order_statistic")
public class OrderStatisticController {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private DishService dishService;

    @Autowired
    private OrderService orderService;


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

    @GetMapping("/restaurant/{id}")
    public ModelAndView showOrderStatisticByRestaurant(@PathVariable("id") Long restaurantId) {
        ModelAndView mv = new ModelAndView("/owner/order/order_by_restaurant");
        List<Orders> orders = (List<Orders>) orderService.findAllByRestaurantId(restaurantId);
        mv.addObject("ordersList", orders);
        return mv;
    }   
}
