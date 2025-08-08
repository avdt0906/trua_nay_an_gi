package com.codegym.project_module_5.service.impl;

import com.codegym.project_module_5.model.Restaurant;
import com.codegym.project_module_5.model.Role;
import com.codegym.project_module_5.model.User;
import com.codegym.project_module_5.model.dto.request.RestaurantRegisterRequest;
import com.codegym.project_module_5.repository.IRestaurantRepository;
import com.codegym.project_module_5.repository.IRoleRepository;
import com.codegym.project_module_5.repository.IUserRepository;
import com.codegym.project_module_5.service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * Tại phương thức registerRestaurant
 * ta có 2 param request, currentUsername
 * request là đối tượng RestaurantRegisterRequest
 * currentUsername là username của người dùng hiện tại
 * phương thức này sẽ tạo ra một đối tượng Restaurant mới
 * và lưu vào database
 * sau đó gửi email thông báo đăng ký nhà hàng thành công
 * có param Email người nhận
 * có param Tên nhà hàng
 */
@Service
public class RestaurantService implements IRestaurantService {
    @Autowired
    IRestaurantRepository restaurantRepository;
    
    @Autowired
    IUserRepository userRepository;
    
    @Autowired
    IRoleRepository roleRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Iterable<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    @Override
    public Optional<Restaurant> findByUsername(String username) {
        return restaurantRepository.findByUser_Username(username);
    }
    
    @Override
    public Restaurant registerRestaurant(RestaurantRegisterRequest request, String currentUsername) {

        Optional<Restaurant> existingRestaurant = findByUsername(currentUsername);
        if (existingRestaurant.isPresent()) {
            throw new RuntimeException("Bạn đã có nhà hàng rồi!");
        }
        

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp!");
        }
        

        Optional<User> currentUser = userRepository.findByUsername(currentUsername);
        if (currentUser.isEmpty()) {
            throw new RuntimeException("Không tìm thấy người dùng!");
        }
        
        User user = currentUser.get();
        

        Optional<Role> ownerRole = roleRepository.findByName("OWNER");
        if (ownerRole.isEmpty()) {
            throw new RuntimeException("Không tìm thấy role OWNER!");
        }
        

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles((Set<Role>) ownerRole.get());
        userRepository.save(user);
        
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setUser(user);
        restaurant.setAddress(request.getAddress());
        restaurant.setPhone(request.getPhone());
        restaurant.setDescription(request.getDescription());
        restaurant.setIs_approved(false);
        restaurant.setIs_open(false);
        restaurant.setIs_locked(false);
        restaurant.setIs_long_term_partner(false);
        
        return restaurantRepository.save(restaurant);
    }
}
