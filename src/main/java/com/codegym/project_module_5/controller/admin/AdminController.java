package com.codegym.project_module_5.controller.admin;

import com.codegym.project_module_5.model.User;
import com.codegym.project_module_5.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        List<User> owners = userService.findAllByRoleName("OWNER");
        model.addAttribute("owners", owners);
        return "admin/dashboard";
    }
}