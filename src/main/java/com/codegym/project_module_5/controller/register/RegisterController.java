package com.codegym.project_module_5.controller.register;


import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import com.codegym.project_module_5.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("")
public class RegisterController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "/login/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerRequest") RegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return "/login/register";
        }
        return "login/login";
    }
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        // Logic to verify the email using the token
        // This is a placeholder, actual implementation will depend on your email verification logic
        model.addAttribute("message", "Email verification successful!");
        return "login/login";
    }
}
