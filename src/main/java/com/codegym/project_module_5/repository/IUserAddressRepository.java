package com.codegym.project_module_5.repository;

import com.codegym.project_module_5.model.UserAddress;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IUserAddressRepository extends JpaRepository<UserAddress, Long> {
    // Additional query methods can be defined here if needed
    List<UserAddress> findAllByUser_Id(Long userId);
    
}
