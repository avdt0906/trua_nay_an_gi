package com.codegym.project_module_5.controller.register;

import com.codegym.project_module_5.model.User;
import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import com.codegym.project_module_5.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        User registered = userService.register(request);
        return ResponseEntity.ok(registered);
    }


}
