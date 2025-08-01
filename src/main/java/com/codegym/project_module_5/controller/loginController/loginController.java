package com.codegym.project_module_5.controller.homeController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {

    @GetMapping("/login")
    public String login(Model model) {
        return "login/login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        return "login/register";
    }
}