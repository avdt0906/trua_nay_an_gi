package com.codegym.project_module_5.controller.admin;

import com.codegym.project_module_5.model.Restaurant;
import com.codegym.project_module_5.model.User;
import com.codegym.project_module_5.service.IOrderService;
import com.codegym.project_module_5.service.IRestaurantService;
import com.codegym.project_module_5.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IRestaurantService restaurantService;

    @GetMapping
    public String redirectToDashboard() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        int ownerCount = userService.findAllByRoleName("OWNER").size();
        int customerCount = userService.findAllByRoleName("CUSTOMER").size();
        long orderCount = orderService.count();

        model.addAttribute("ownerCount", ownerCount);
        model.addAttribute("customerCount", customerCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("activePage", "dashboard");

        return "admin/dashboard";
    }

    @GetMapping("/list")
    public String showOwnerList(Model model) {
        List<User> owners = userService.findAllByRoleName("OWNER");
        model.addAttribute("owners", owners);
        model.addAttribute("activePage", "list");

        return "admin/list";
    }

    @GetMapping("/owner/{id}")
    public String showOwnerDetail(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> ownerOptional = userService.findById(id);

        if (ownerOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy chủ nhà hàng với ID: " + id);
            return "redirect:/admin/list";
        }

        User owner = ownerOptional.get();
        Optional<Restaurant> restaurantOptional = restaurantService.findByUsername(owner.getUsername());

        model.addAttribute("owner", owner);
        model.addAttribute("restaurant", restaurantOptional.orElse(null));
        model.addAttribute("activePage", "list");

        return "admin/owner_detail";
    }
}
