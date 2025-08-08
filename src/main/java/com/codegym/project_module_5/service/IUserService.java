package com.codegym.project_module_5.service;

import com.codegym.project_module_5.model.Restaurant;
import com.codegym.project_module_5.model.User;
import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface IUserService extends IGeneralService<User> {
    Optional<User> findByUsername(String username);
    User register(RegisterRequest request);
//    List<User> findAllByRoleName(String roleName);
}