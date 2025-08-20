package com.codegym.project_module_5.service.user_service;

import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import com.codegym.project_module_5.service.general_service.IGeneralService;

import java.util.List;
import java.util.Optional;


public interface IUserService extends IGeneralService<User> {
    Optional<User> findByUsername(String username);
    User register(RegisterRequest request);
    List<User> findAllByRoleName(String roleName);
    boolean existsByEmail(String username);
    boolean existsByUsername(String username);
}