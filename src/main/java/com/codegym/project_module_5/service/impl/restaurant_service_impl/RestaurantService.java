package com.codegym.project_module_5.service.impl.restaurant_service_impl;

import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.restaurant_model.Coupon;
import com.codegym.project_module_5.model.user_model.Role;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.model.dto.request.RestaurantRegisterRequest;
import com.codegym.project_module_5.repository.restaurant_repository.IRestaurantRepository;
import com.codegym.project_module_5.repository.user_repository.IRoleRepository;
import com.codegym.project_module_5.repository.user_repository.IUserRepository;
import com.codegym.project_module_5.service.impl.user_service_impl.EmailService;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RestaurantService implements IRestaurantService {

    @Autowired
    private IRestaurantRepository iRestaurantRepository;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public RestaurantService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public Optional<Restaurant> findByUsername(String username) {
        return iRestaurantRepository.findByUsername(username);
    }

    @Override
    public Restaurant registerRestaurant(RestaurantRegisterRequest request, String currentUsername) {

        if (request == null) {
            throw new IllegalArgumentException("Request data cannot be null");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu không khớp với xác nhận mật khẩu");
        }

        if (iUserRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại trong hệ thống");
        }

        if (iUserRepository.existsByUsername(request.getName())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }

        try {
            User user = new User();
            user.setUsername(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhone(request.getPhone());
            user.setFullName(request.getName());

            Role restaurantRole = iRoleRepository.findByName("OWNER")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò OWNER"));
            user.setRoles(Set.of(restaurantRole));

            User savedUser = iUserRepository.save(user);
            log.info("User created successfully with ID: {}", savedUser.getId());

            Restaurant restaurant = new Restaurant();
            restaurant.setName(request.getName());
            restaurant.setUser(savedUser);
            restaurant.setAddress(request.getAddress());
            restaurant.setPhone(request.getPhone());
            restaurant.setDescription(request.getDescription());
            restaurant.setIsOpen(false);
            restaurant.setIsLocked(false);
            restaurant.setIsLongTermPartner(false);


            Restaurant savedRestaurant = iRestaurantRepository.save(restaurant);
            log.info("Restaurant created successfully with ID: {}", savedRestaurant.getId());

            return savedRestaurant;

        } catch (Exception e) {
            log.error("Error creating restaurant: {}", e.getMessage(), e);
            throw new RuntimeException("Đăng ký nhà hàng thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public Iterable<Restaurant> findAll() {
        return iRestaurantRepository.findAll();
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return iRestaurantRepository.findById(id);
    }

    @Override
    public void save(Restaurant restaurant) {
        iRestaurantRepository.save(restaurant);
    }

    @Override

    public List<Coupon> getCouponsByRestaurantId(Long restaurantId) {
        return iRestaurantRepository.findCouponsByRestaurantId(restaurantId);
        }


    @Override
    public Restaurant toggleLockStatus(Long restaurantId) {
        Restaurant restaurant = iRestaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhà hàng với ID: " + restaurantId));
        restaurant.setIsLocked(!restaurant.getIsLocked());
        return iRestaurantRepository.save(restaurant);
    }
    @Override
    public void delete(Long id) {
        iRestaurantRepository.deleteById(id);

    }

    @Override
    public List<Restaurant> getPendingApprovalRestaurants() {
        return iRestaurantRepository.findByIsApprovedIsNull();
    }
}
