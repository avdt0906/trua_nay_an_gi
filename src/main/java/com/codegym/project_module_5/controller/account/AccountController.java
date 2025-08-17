package com.codegym.project_module_5.controller.account;

import com.codegym.project_module_5.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
}


