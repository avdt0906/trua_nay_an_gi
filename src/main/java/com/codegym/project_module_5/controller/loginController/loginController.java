package com.codegym.project_module_5.controller.loginController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {

    @GetMapping("/home")
    public String login(Model model) {
        return "login/login";
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