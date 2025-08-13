package com.codegym.project_module_5.repository.user_repository;


import java.util.List;

import com.codegym.project_module_5.model.user_model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IUserAddressRepository extends JpaRepository<UserAddress, Long> {
    // Additional query methods can be defined here if needed
    List<UserAddress> findAllByUser_Id(Long userId);
    
}
