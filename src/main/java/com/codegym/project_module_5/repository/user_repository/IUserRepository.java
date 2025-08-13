package com.codegym.project_module_5.repository.user_repository;

import com.codegym.project_module_5.model.user_model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findAllByRoles_Name(String roleName);
}
