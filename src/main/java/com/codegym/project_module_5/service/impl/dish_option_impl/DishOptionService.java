package com.codegym.project_module_5.service.impl.dish_option_impl;

import com.codegym.project_module_5.model.restaurant_model.DishOption;
import com.codegym.project_module_5.repository.dish_option_repository.IDishOptionRepository;
import com.codegym.project_module_5.service.dish_option_service.IDishOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishOptionService implements IDishOptionService {

    @Autowired
    private IDishOptionRepository iDishOptionRepository;
    @Override
    public Iterable<DishOption> findAll() {
        return iDishOptionRepository.findAll();
    }

    @Override
    public Optional<DishOption> findById(Long id) {
        return iDishOptionRepository.findById(id);
    }

    @Override
    public void save(DishOption dishOption) {
        iDishOptionRepository.save(dishOption);
    }

    @Override
    public void delete(Long id) {
        iDishOptionRepository.deleteById(id);
    }

    @Override
    public List<DishOption> findDishOptionByDishId(Long id) {
        return iDishOptionRepository.findByDish_Id(id);
    }
}
