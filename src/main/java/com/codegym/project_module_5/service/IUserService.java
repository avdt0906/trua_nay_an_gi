package com.codegym.project_module_5.service;

import com.codegym.project_module_5.model.User;
import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import org.springframework.stereotype.Service;


public interface IUserService extends IGeneralService<User> {
    User register(RegisterRequest request);
}
