package com.codegym.project_module_5.controller.account;

import com.codegym.project_module_5.model.User;
import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import com.codegym.project_module_5.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "account/login";
    }

    @GetMapping("/forgot_password")
    public String forgotPassword(Model model) {
        return "account/forgot_password";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "account/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerRequest") RegisterRequest request, Model model) {
        User registered = userService.register(request);
        model.addAttribute("registerRequest", registered);
        return "account/login";
    }
}


