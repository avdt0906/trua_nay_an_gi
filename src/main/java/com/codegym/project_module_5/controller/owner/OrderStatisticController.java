package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.service.impl.order_service_impl.OrderDetailService;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/order_statistic")
public class OrderStatisticController {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private IDishService dishService;

    @GetMapping("/{id}")
    public ModelAndView showOrderStatistic(@PathVariable Long id, Model model) {
        List<OrderDetail> orderDetailList = (List<OrderDetail>) orderDetailService.findAllByOrderId(id);
        ModelAndView modelAndView = new ModelAndView("/owner/order/order_statistic");
        modelAndView.addObject("orderDetailList", orderDetailList);
        return modelAndView;
    }

    @GetMapping("/dish/{dishId}")
    public ModelAndView showOrderStatisticByDish(@PathVariable("dishId") Long dishId) {
        List<OrderDetail> orderDetailList = orderDetailService.findByDishId(dishId);
        ModelAndView mv = new ModelAndView("/owner/order/order_statistic");
        mv.addObject("orderDetailList", orderDetailList);
        java.util.Optional<Dish> dishOpt = dishService.findById(dishId);
        dishOpt.ifPresent(d -> mv.addObject("dishName", d.getName()));
        return mv;
    }
}
