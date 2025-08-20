package com.codegym.project_module_5.controller.register;

import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import com.codegym.project_module_5.service.impl.user_service_impl.OtpService;
import com.codegym.project_module_5.service.impl.user_service_impl.UserService;

import jakarta.servlet.http.HttpSession;
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
    @Autowired
    private OtpService otpService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "/account/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerRequest") RegisterRequest request, BindingResult result,
            HttpSession session, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registerRequest", request);
            return "account/register";
        }
        if (userService.existsByUsername(request.getUsername())) {
            result.rejectValue("username", "error.username", "Tên đăng nhập đã được sử dụng");
            return "account/register";
        }
        // Kiểm tra email đã tồn tại
        if (userService.existsByEmail(request.getEmail())) {
            result.rejectValue("email", "error.email", "Email đã được sử dụng");
            return "account/register";
        }

        otpService.generateAndSendOtp(request.getEmail(), request.getFullName());
        session.setAttribute("pendingRegister", request);
        System.out.println("Pending Register: " + request);
        return "account/verify";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,
            HttpSession session,
            Model model) {

        RegisterRequest request = (RegisterRequest) session.getAttribute("pendingRegister");
        if (request == null) {
            model.addAttribute("error", "Không tìm thấy thông tin đăng ký, vui lòng thử lại.");
            return "account/register";
        }

        String email = request.getEmail();

        boolean isValid = otpService.verifyOtp(email, otp);

        if (!isValid) {
            model.addAttribute("error", "Mã OTP không hợp lệ hoặc đã hết hạn");
            return "account/verify";
        }

        userService.register(request);
        session.removeAttribute("pendingRegister");

        return "redirect/login";
    }

}
