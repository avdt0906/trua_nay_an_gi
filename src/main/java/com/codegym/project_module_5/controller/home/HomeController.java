package com.codegym.project_module_5.controller.home;

import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("")
public class HomeController {

    @Autowired
    private IUserService userService;

    @GetMapping(value = {"/", "/home"})
    public String showhome(Model model, @RequestParam(value = "success", required = false) String success) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            String username = authentication.getName();
            Optional<User> userOptional = userService.findByUsername(username);
            userOptional.ifPresent(user -> model.addAttribute("currentUser", user));
        }

        // Xử lý thông báo thành công
        if ("restaurant_registered".equals(success)) {
            model.addAttribute("showSuccessMessage", true);
            model.addAttribute("successMessage", "Đăng ký nhà hàng thành công! Nhà hàng của bạn đang chờ admin duyệt để mở cửa.");
        }

        return "/homepage/index";
    }
}
