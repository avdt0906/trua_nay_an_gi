package com.codegym.project_module_5.service.impl;

import com.codegym.project_module_5.model.Restaurant;
import com.codegym.project_module_5.repository.IRestaurantRepository;
import com.codegym.project_module_5.service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantService implements IRestaurantService {

    private final EmailService emailService;
    @Autowired
    IRestaurantRepository restaurantRepository;

    RestaurantService(EmailService emailService) {
        this.emailService = emailService;
    }

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
    public boolean approveRestaurant(Long restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.setIs_approved(true);
            restaurantRepository.save(restaurant);

            String toEmail = restaurant.getUser().getEmail();
            String restaurantName = restaurant.getName();
            emailService.sendApprovalEmail(toEmail, restaurantName);
            return true;
        }
        return false;
    }

    @Override
    public boolean rejectRestaurant(Long restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.setIs_approved(false);
            restaurantRepository.save(restaurant);

            String toEmail = restaurant.getUser().getEmail();
            String restaurantName = restaurant.getName();
            emailService.sendRejectionEmail(toEmail, restaurantName);
            return true;
        }
        return false;
    }

    @Override
    public boolean toggleRestaurantApproval(Long restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.setIs_approved(!restaurant.getIs_approved());
            restaurantRepository.save(restaurant);
            return true;
        }
        return false;
    }
}
