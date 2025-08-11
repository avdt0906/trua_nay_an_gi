package com.codegym.project_module_5.service;

import com.codegym.project_module_5.model.User;
import com.codegym.project_module_5.model.dto.request.RegisterRequest;

import java.util.Optional;


public interface IUserService extends IGeneralService<User> {
    Optional<User> findByUsername(String username);
    User register(RegisterRequest request);
    boolean existsByEmail(String username);
//    List<User> findAllByRoleName(String roleName);
}