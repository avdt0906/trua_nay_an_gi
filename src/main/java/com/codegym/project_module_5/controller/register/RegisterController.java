package com.codegym.project_module_5.controller.register;

import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import com.codegym.project_module_5.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegisterController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "signup";
    }

//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute("registerRequest") RegisterRequest request, Model model) {
//        User registered = userService.register(request);
//        model.addAttribute("user", registered);
//        return "/register_success";
//    }
}
