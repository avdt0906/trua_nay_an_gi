package com.codegym.project_module_5.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class LoginController {
    @GetMapping("/login")
    public String login(Model model) {
        return "login/login";
    }
    @GetMapping("/home")
    public String home(Model model) {
        return "login/home";
    }
    @GetMapping("/register")
    public String register(Model model) {
        return "login/register";
    }
    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model) {
        return "login/forgotPassword";
    }
}
