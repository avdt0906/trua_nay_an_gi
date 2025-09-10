package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.dto.request.RestaurantRegisterRequest;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import com.codegym.project_module_5.repository.order_repository.IOrderRepository;
import com.codegym.project_module_5.repository.order_repository.IOrderDetailRepository;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.order_model.OrderDetail;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private IRestaurantService restaurantService;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderDetailRepository orderDetailRepository;


    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("restaurant", new RestaurantRegisterRequest());
        return "owner/restaurant/register_restaurant";
    }

    @PostMapping("/signup")
    public String registerRestaurant(
            @Valid @ModelAttribute("restaurant") RestaurantRegisterRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurant", request);
            return "owner/restaurant/register_restaurant";
        }

        try {
            String currentUsername = principal != null ? principal.getName() : null;

            restaurantService.registerRestaurant(request, currentUsername);

            return "redirect:/?success=restaurant_registered";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "owner/restaurant/register_restaurant";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đăng ký thất bại: " + e.getMessage());
            return "owner/restaurant/register_restaurant";
        }
    }

    @GetMapping("/dashboard")
    public ModelAndView showDashboard() {
        ModelAndView mv = new ModelAndView("owner/restaurant/dashboard");
        Optional<Restaurant> restaurant = restaurantService.findByUsername(getCurrentUsername());
        
        if (restaurant.isPresent()) {
            Restaurant restaurantData = restaurant.get();
            mv.addObject("restaurant", restaurantData);
            
            double totalRevenue = calculateTestRevenue(restaurantData.getId());
            mv.addObject("totalRevenue", totalRevenue);
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

    private double calculateTestRevenue(Long restaurantId) {
        
        List<Orders> orders = (List<Orders>) orderRepository.findAllByRestaurantId(restaurantId);
        
        double totalRevenue = 0.0;
        
        for (Orders order : orders) {
            System.out.println("Order ID: " + order.getId() + ", Status: " + order.getOrderStatus().getName());
            
            double orderAmount = 0;
            List<OrderDetail> details = (List<OrderDetail>) orderDetailRepository.findAllByOrderId(order.getId());
            
            for (OrderDetail detail : details) {
                double itemTotal = detail.getDish().getPrice() * detail.getQuantity();
                orderAmount += itemTotal;
            }
            
            double netAmount = orderAmount - 15000;
            double commission = netAmount >= 200_000_000 ? 0.10 : netAmount <= 100_000_000 ? 0.05 : 0.075;
            double orderRevenue = netAmount * (1 - commission);
            totalRevenue += orderRevenue;
            
        }

        return totalRevenue;
    }
}
