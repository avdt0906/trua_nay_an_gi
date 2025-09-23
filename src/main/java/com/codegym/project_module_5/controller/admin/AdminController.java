package com.codegym.project_module_5.controller.admin;

import com.codegym.project_module_5.model.order_model.OrderStatus;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.user_model.Role;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.repository.order_repository.IOrderStatusRepository;
import com.codegym.project_module_5.service.Banner.IBannerService;
import com.codegym.project_module_5.service.impl.order_service_impl.OrderDetailService;
import com.codegym.project_module_5.service.impl.role_service_impl.RoleService;
import com.codegym.project_module_5.service.order_service.IOrderService;
import com.codegym.project_module_5.service.restaurant_service.IDishService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import com.codegym.project_module_5.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Sort;

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
    @Autowired
    private IDishService dishService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private IOrderStatusRepository orderStatusRepository;
    @Autowired
    private IBannerService bannerService;
    @Autowired
    private OrderDetailService orderDetailService;

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
        List<Dish> topDishes = dishService.findTop8ByOrderByDiscountDesc();

        model.addAttribute("ownerCount", owners != null ? owners.size() : 0);
        model.addAttribute("customerCount", customers != null ? customers.size() : 0);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("topDishes", topDishes);

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

    @GetMapping("/restaurants/partner-requests")
    public String getPartnerRequests(Model model) {
        List<Restaurant> partnerRequests = restaurantService.getPartnerRequests();
        model.addAttribute("partnerRequests", partnerRequests);
        return "admin/partner_pending_list";
    }

    @PostMapping("/restaurants/approvals/partner/approve/{id}")
    public String approvePartnerRequest(@PathVariable Long id) {
        restaurantService.approvePartner(id);
        return "redirect:/admin/restaurants/partner-requests";
    }

    @PostMapping("/restaurant/approvals/partner/reject/{id}")
    public String rejectPartnerRequest(@PathVariable Long id) {
        restaurantService.rejectPartner(id);
        return "redirect:/admin/restaurants/partner-requests";
    }

    @GetMapping("/users/list")
    public String listUsers(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        Page<User> usersPage = userService.findAllUsers(page, size);

        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", usersPage.getNumber());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("totalItems", usersPage.getTotalElements());
        model.addAttribute("activePage", "list");

        return "admin/user_list";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users/list";
    }

    // Hiển thị form
    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Role> allRoles = roleService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "admin/user_edit";
    }

    // Xử lý cập nhật
    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id,
            @ModelAttribute("user") User formUser) {
        // Lưu ý: Bạn có thể cần nạp user cũ, merge role, hoặc encode password
        userService.updateUser(id, formUser);
        return "redirect:/admin/users/list";
    }

    @GetMapping("/dish/list")
    public String listDishes(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        int pageSize = 10;
        Page<Dish> dishPage;

        if (!keyword.isBlank()) {
            dishPage = dishService.search(keyword, PageRequest.of(page, pageSize));
        } else {
            dishPage = dishService.findAll(PageRequest.of(page, pageSize));
        }

        model.addAttribute("dishes", dishPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", dishPage.getTotalPages());
        model.addAttribute("keyword", keyword); // giờ luôn là chuỗi, không null

        return "admin/dish_list";
    }

    @GetMapping("/orders/list")
    public String listOrders(
            @RequestParam(required = false) Long statusId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Page<Orders> page = (statusId != null)
                ? orderService.findAllByOrderStatus_Id(statusId, pageable)
                : orderService.findAll(pageable);

        model.addAttribute("ordersPage", page);
        model.addAttribute("orderStatuses", orderStatusRepository.findAll());
        model.addAttribute("selectedStatus", statusId);
        return "admin/orders_list";
    }

    @PostMapping("/orders/updateStatus")
    public ModelAndView updateOrders(@RequestParam Long orderId, @RequestParam OrderStatus newStatus) {
        Optional<Orders> ordersOptional = orderService.findById(orderId);
        Orders order = ordersOptional.get();
        order.setOrderStatus(newStatus);
        orderService.save(order);
        return new ModelAndView("redirect:/admin/orders/list");
    }

    @PostMapping("/banner/update/{dishId}")
    public String updateBanner(@PathVariable Long dishId,
                               @RequestParam(required = false, defaultValue = "false") boolean featured,
                               @RequestParam(required = false, defaultValue = "false") boolean promotion,
                               RedirectAttributes redirect) {

        bannerService.updateBannerForDish(dishId, featured, promotion);
        redirect.addFlashAttribute("message", "Cập nhật banner thành công!");
        return "redirect:/admin/dish/list";
    }

}