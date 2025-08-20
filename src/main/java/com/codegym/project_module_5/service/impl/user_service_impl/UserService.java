package com.codegym.project_module_5.service.impl.user_service_impl;

import com.codegym.project_module_5.model.user_model.Role;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.model.dto.request.RegisterRequest;
import com.codegym.project_module_5.model.user_model.UserAddress;
import com.codegym.project_module_5.repository.user_repository.IUserAddressRepository;
import com.codegym.project_module_5.repository.user_repository.IRoleRepository;
import com.codegym.project_module_5.repository.user_repository.IUserRepository;
import com.codegym.project_module_5.service.user_service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserAddressRepository userAddressRepository;

    @Override
    public User register(RegisterRequest request) {
        Role userRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        Set<Role> userRoles = Set.of(userRole);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        user.setRoles(userRoles);
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllByRoleName(String roleName) {
        return userRepository.findAllByRoles_Name(roleName);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

  public void updateAvatar(String username, String avatarUrl) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
    }

    public void updateUserInfo(String username, User updatedUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFullName(updatedUser.getFullName());
        user.setPhone(updatedUser.getPhone());
        userRepository.save(user);
    }

    public void addAddress(Long userId, String address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserAddress ua = new UserAddress();
        ua.setUser(user);
        ua.setAddress(address);
        userAddressRepository.save(ua);
    }

    public void deleteAddress(Long addressId) {
        userAddressRepository.deleteById(addressId);
    }

    public List<UserAddress> getUserAddresses(Long userId) {
        return userAddressRepository.findAllByUser_Id(userId);
    }

    public UserAddress updateUserAddress(Long addressId, String newAddress) {
        // 1. Tìm địa chỉ theo ID
        UserAddress userAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // 2. Cập nhật địa chỉ
        userAddress.setAddress(newAddress);

        // 3. Lưu lại
        return userAddressRepository.save(userAddress);
    }
}
