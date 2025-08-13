package com.codegym.project_module_5.controller.client;

import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.model.user_model.UserAddress;
import com.codegym.project_module_5.service.impl.user_service_impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class UserAddressController {

    @Autowired
    private UserService userService;

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    @GetMapping("")
    public String listAddresses(Model model) {
        String username = getCurrentUsername();
        // Lấy User từ Optional
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Gọi từ AddressService thay vì UserService
        model.addAttribute("addresses", userService.getUserAddresses(currentUser.getId()));
        model.addAttribute("newAddress", new UserAddress());
        return "user/address_list";
    }

    @PostMapping("/add")
    public String addAddress(@ModelAttribute("newAddress") UserAddress newAddress) {
        String username = getCurrentUsername();
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userService.addAddress(currentUser.getId(), newAddress.getAddress());
        return "redirect:/addresses";
    }


    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id) {
        userService.deleteAddress(id);
        return "redirect:/addresses";
    }

    @PostMapping("/update/{id}")
    public String updateAddress(@PathVariable Long id,
                            @RequestParam String address) {
    userService.updateUserAddress(id, address);
    return "redirect:/addresses"; // đổi sang trang danh sách của bạn
}

}
