package com.codegym.project_module_5.controller.owner;

import com.codegym.project_module_5.model.dto.request.RestaurantRegisterRequest;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import com.codegym.project_module_5.service.restaurant_service.IWalletWithdrawService;
import com.codegym.project_module_5.repository.order_repository.IOrderRepository;
import com.codegym.project_module_5.repository.order_repository.IOrderDetailRepository;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.order_model.OrderDetail;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    @Value("${mapbox.api.key}")
    private String mapboxApiKey;

    @Autowired
    private IWalletWithdrawService walletWithdrawService;

    @Autowired
    private IRestaurantService restaurantService;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("mapboxApiKey", mapboxApiKey);
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
            String status = order.getOrderStatus().getName();

            if (status.equals("Đã xác nhận")) {
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

        }

        return totalRevenue;
    }

    @GetMapping("/wallet")
    public String viewWallet(Model model, Principal principal) {

        String username = principal.getName();

        Restaurant restaurant = restaurantService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy nhà hàng cho user: " + username));

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("requests",
                walletWithdrawService.getRequestsByRestaurant(restaurant.getId()));

        return "owner/wallet/walletindex";
    }

    @PostMapping("/wallet/withdraw")
    public String createWithdrawRequest(@RequestParam("amount") Double amount,
            @RequestParam(value = "note", required = false) String note,
            Principal principal,
            RedirectAttributes redirect) {
        Restaurant restaurant = restaurantService
                .findByUsername(getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Tạo yêu cầu rút tiền
        walletWithdrawService.createWithdrawRequest(restaurant.getId(), amount, note);

        redirect.addFlashAttribute("success", "Đã gửi yêu cầu rút tiền!");
        return "redirect:/restaurants/wallet";
    }

    @GetMapping("/contract")
    public String showContract(Model model) {
        Restaurant restaurant = restaurantService
                .findByUsername(getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        model.addAttribute("restaurant", restaurant);

        Double monthlyRevenue = restaurantService.getMonthlyRevenue(restaurant.getId());
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        boolean canTerminate = monthlyRevenue >= 100_000_000;
        model.addAttribute("canTerminate", canTerminate);

        return "owner/restaurant/contract";
    }

    @GetMapping("/monthlyrevenue")
    public String viewMonthlyRevenue(Model model) {
        Restaurant restaurant = restaurantService
                .findByUsername(getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Double monthlyRevenue = restaurantService.getMonthlyRevenue(restaurant.getId());
        model.addAttribute("monthlyRevenue", monthlyRevenue);

       
        List<Orders> orders = orderRepository.findAllByRestaurantId(restaurant.getId());
        model.addAttribute("orders", orders);

        return "owner/restaurant/monthlyrevenue"; // Tạo template monthlyRevenue.html
    }

    @PostMapping("/terminateContract")
    public String terminateContract(RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService
                .findByUsername(getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        try {
            restaurantService.terminateContract(restaurant.getId());
            redirectAttributes.addFlashAttribute("success", "Thanh lý hợp đồng thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/restaurants/dashboard"; // quay lại trang dashboard
    }

}
