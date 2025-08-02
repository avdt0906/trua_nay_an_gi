package com.codegym.project_module_5.service.impl;

import com.codegym.project_module_5.model.Role;
import com.codegym.project_module_5.model.User;
import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import com.codegym.project_module_5.repository.IRoleRepository;
import com.codegym.project_module_5.repository.IUserRepository;
import com.codegym.project_module_5.service.IGeneralService;
import com.codegym.project_module_5.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @Autowired
    private  IUserRepository iUserRepository;

    @Autowired
    private  IRoleRepository iRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterRequest request) {
        if (iUserRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Role userRole = iRoleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        user.setAvatar_url(request.getAvatar_url());
        user.setRole(userRole);

        return iUserRepository.save(user);
    }
}
