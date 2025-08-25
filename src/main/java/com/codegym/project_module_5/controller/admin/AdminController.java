package com.codegym.project_module_5.controller.admin;

import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.shipper_model.Shipper;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.order_service.IOrderService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import com.codegym.project_module_5.service.shipper_service.IShipperService;
import com.codegym.project_module_5.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * Chuyển hướng từ /admin sang /admin/dashboard.
     * 
     * @return Chuỗi chuyển hướng.
     */
    @GetMapping
    public String redirectToDashboard() {
        return "redirect:/admin/dashboard";
    }

    /**
     * Hiển thị trang tổng quan (dashboard) chính của admin.
     * 
     * @param model Model để truyền dữ liệu tới view.
     * @return Tên view của trang dashboard.
     */
    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        // FindAllByRoleName is used here, so it needs to be uncommented
        List<User> owners = userService.findAllByRoleName("OWNER");
        List<User> customers = userService.findAllByRoleName("CUSTOMER");
        long orderCount = orderService.count();

        model.addAttribute("ownerCount", owners != null ? owners.size() : 0);
        model.addAttribute("customerCount", customers != null ? customers.size() : 0);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("activePage", "dashboard");

        return "admin/dashboard";
    }

    /**
     * Hiển thị danh sách các chủ nhà hàng và trạng thái nhà hàng của họ.
     * 
     * @param model Model để truyền dữ liệu tới view.
     * @return Tên view của trang danh sách.
     */
    @GetMapping("/list")
    public String showOwnerList(Model model) {
        List<User> owners = userService.findAllByRoleName("OWNER");
        Map<Long, Restaurant> restaurantMap = new HashMap<>();
        for (User owner : owners) {
            restaurantService.findByUsername(owner.getUsername())
                    .ifPresent(restaurant -> restaurantMap.put(owner.getId(), restaurant));
        }

        model.addAttribute("owners", owners);
        model.addAttribute("restaurantMap", restaurantMap); // Truyền Map nhà hàng tới view
        model.addAttribute("activePage", "list");

        return "admin/list";
    }

    /**
     * Hiển thị trang chi tiết thông tin của một chủ nhà hàng và nhà hàng của họ.
     * 
     * @param id                 ID của chủ nhà hàng (User).
     * @param model              Model để truyền dữ liệu tới view.
     * @param redirectAttributes Dùng để gửi thông báo lỗi nếu không tìm thấy.
     * @return Tên view của trang chi tiết hoặc chuyển hướng về trang danh sách.
     */
    @GetMapping("/owner/{id}")
    public String showOwnerDetail(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> ownerOptional = userService.findById(id);

        if (ownerOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy chủ nhà hàng với ID: " + id);
            return "redirect:/admin/list";
        }

        User owner = ownerOptional.get();
        // Tìm nhà hàng dựa trên username của chủ sở hữu
        Optional<Restaurant> restaurantOptional = restaurantService.findByUsername(owner.getUsername());

        model.addAttribute("owner", owner);
        // Ngay cả khi không có nhà hàng, vẫn truyền một Optional rỗng để view xử lý
        model.addAttribute("restaurant", restaurantOptional.orElse(null));
        model.addAttribute("activePage", "list"); // Giữ cho mục "Quản lý Chủ quán" active

        return "admin/owner_detail";
    }

    @PostMapping("/restaurant/toggle-lock/{id}")
    public String toggleRestaurantLock(@PathVariable("id") Long restaurantId, RedirectAttributes redirectAttributes) {
        try {
            Restaurant updatedRestaurant = restaurantService.toggleLockStatus(restaurantId);
            String status = updatedRestaurant.getIsLocked() ? "khóa" : "mở khóa";
            String message = "Đã " + status + " nhà hàng '" + updatedRestaurant.getName() + "' thành công.";
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/list";
    }

    @GetMapping("/restaurants/pending")
    public String getPendingRestaurants(Model model) {
        List<Restaurant> pendingRestaurants = restaurantService.getPendingApprovalRestaurants();
        model.addAttribute("pendingRestaurants", pendingRestaurants);
        return "admin/approval_list";
    }

    @PostMapping("/restaurants/approve/{id}")
    public String approveRestaurant(@PathVariable Long id) {
        Optional<Restaurant> optional = restaurantService.findById(id);
        if (optional.isPresent()) {
            Restaurant restaurant = optional.get();
            restaurant.setIsApproved(true);
            restaurantService.save(restaurant);
        }
        return "redirect:/admin/restaurants/pending";
    }

    @PostMapping("/restaurants/reject/{id}")
    public String rejectRestaurant(@PathVariable Long id) {
        Optional<Restaurant> optional = restaurantService.findById(id);
        if (optional.isPresent()) {
            Restaurant restaurant = optional.get();
            restaurant.setIsApproved(false);
            restaurantService.save(restaurant);
        }
        return "redirect:/admin/restaurants/pending";
    }

}