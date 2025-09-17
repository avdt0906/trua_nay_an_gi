package com.codegym.project_module_5.service.user_service;

import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import com.codegym.project_module_5.model.user_model.UserAddress;
import com.codegym.project_module_5.service.general_service.IGeneralService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface IUserService extends IGeneralService<User> {
    Optional<User> findByUsername(String username);
    User register(RegisterRequest request);
    List<User> findAllByRoleName(String roleName);
    boolean existsByEmail(String username);
    boolean existsByUsername(String username);
    List<UserAddress> getUserAddresses(Long userId);
    void deleteById(Long id);
    Page<User> findAllUsers(int page, int size);
    User updateUser(Long id, User updatedUser);

}