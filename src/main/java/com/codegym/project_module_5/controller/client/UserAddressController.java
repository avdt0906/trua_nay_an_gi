package com.codegym.project_module_5.controller.client;

import com.codegym.project_module_5.model.dto.request.UserAddressRequest;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.model.user_model.UserAddress;
import com.codegym.project_module_5.service.impl.user_service_impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${mapbox.api.key}")
    private String mapboxApiKey;

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
        model.addAttribute("addresses", userService.getUserAddresses(currentUser.getId()));
        return "user/address_list";
    }

    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id) {
        userService.deleteAddress(id);
        return "redirect:/addresses";
    }

    @GetMapping("/new")
    public String showAddAddressForm(Model model) {
        model.addAttribute("addressForm", new UserAddressRequest());
        model.addAttribute("mapboxApiKey", mapboxApiKey); // Lấy từ file cấu hình
        return "user/address_form";
    }

    @PostMapping("/save")
    public String saveAddress(@ModelAttribute("addressForm") UserAddressRequest addressForm) {
        String username = getCurrentUsername();
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userService.adddAdress(currentUser.getId(), addressForm);
        return "redirect:/addresses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserAddress address = userService.getAddressById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        model.addAttribute("address", address);
        model.addAttribute("mapboxApiKey", mapboxApiKey);
        return "user/edit_address";
    }

   @PostMapping("/update/{id}")
public String updateAddress(@PathVariable Long id, @ModelAttribute UserAddressRequest addressRequest) {
    userService.updateAddress(id, addressRequest);
    return "redirect:/addresses";

    

}
}
