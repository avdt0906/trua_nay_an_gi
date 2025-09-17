package com.codegym.project_module_5.service.impl.role_service_impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codegym.project_module_5.model.user_model.Role;
import com.codegym.project_module_5.repository.user_repository.IRoleRepository;
@Service
public class RoleService {
 private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    
}
