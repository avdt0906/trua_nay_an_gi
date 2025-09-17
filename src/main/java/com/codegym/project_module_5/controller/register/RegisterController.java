package com.codegym.project_module_5.controller.register;

import com.codegym.project_module_5.common.OtpStatus;
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
    public String registerUser(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            HttpSession session,
            Model model) {
        if (result.hasErrors()) {
            return "account/register";
        }
        if (userService.existsByUsername(request.getUsername())) {
            result.rejectValue("username", "error.username", "Tên đăng nhập đã được sử dụng");
            return "account/register";
        }
        if (userService.existsByEmail(request.getEmail())) {
            result.rejectValue("email", "error.email", "Email đã được sử dụng");
            return "account/register";
        }

        // Gửi OTP
        otpService.generateAndSendOtp(request.getEmail(), request.getFullName());
        session.setAttribute("pendingRegister", request);

        // Redirect thay vì return view trực tiếp
        return "redirect:/verify-otp";
    }

    @GetMapping("/verify-otp")
    public String showVerifyOtpPage(HttpSession session, Model model) {
        RegisterRequest request = (RegisterRequest) session.getAttribute("pendingRegister");
        if (request == null) {
            model.addAttribute("error", "Không tìm thấy thông tin đăng ký, vui lòng thử lại.");
            return "account/register";
        }
        model.addAttribute("email", request.getEmail());
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
        OtpStatus status = otpService.verifyOtp(email, otp);

        switch (status) {
            case VALID -> {
                userService.register(request);
                session.removeAttribute("pendingRegister");
                return "redirect:/login";
            }
            case EXPIRED -> {
                model.addAttribute("error", "Mã OTP đã hết hạn, vui lòng yêu cầu gửi lại.");
                model.addAttribute("email", email); // giữ email để resend OTP
                return "account/verify";
            }
            case INVALID -> {
                model.addAttribute("error", "Mã OTP không hợp lệ.");
                model.addAttribute("email", email);
                return "account/verify";
            }
            default -> {
                model.addAttribute("error", "Lỗi không xác định.");
                model.addAttribute("email", email);
                return "account/verify";
            }
        }
    }

}
