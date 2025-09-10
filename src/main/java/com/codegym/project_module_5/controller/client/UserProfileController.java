package com.codegym.project_module_5.controller.client;

import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.impl.user_service_impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.codegym.project_module_5.model.dto.request.UserProfileRequest;
import com.codegym.project_module_5.service.impl.user_service_impl.FileStorageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class UserProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileStorageService fileStorageService;

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    @GetMapping("/edit")
    public String showEditProfileForm(Model model) {
        String username = getCurrentUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileRequest dto = new UserProfileRequest();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatarUrl(user.getAvatarUrl());
        model.addAttribute("user", dto);
        return "user/edit_profile";
    }

    @PostMapping("/update-avatar")
    public String updateAvatar(@RequestParam("avatarFile") MultipartFile avatarFile) {
        String username = getCurrentUsername();
        try {
            if (!avatarFile.isEmpty()) {
                String avatarUrl = fileStorageService.saveAvatar(avatarFile);
                if (avatarUrl != null) {
                    userService.updateAvatar(username, avatarUrl);
                    return "redirect:/profile/edit?avatarUpdated";
                } else {
                    return "redirect:/profile/edit?avatarError";
                }
            }
            return "redirect:/profile/edit?avatarError";
        } catch (Exception e) {
            return "redirect:/profile/edit?avatarError";
        }
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser) {
        String username = getCurrentUsername();
        userService.updateUserInfo(username, updatedUser);
        return "redirect:/profile/edit?success";
    }
}
