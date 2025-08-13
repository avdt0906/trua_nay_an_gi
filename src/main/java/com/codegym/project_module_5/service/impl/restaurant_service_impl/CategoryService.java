package com.codegym.project_module_5.service.impl.restaurant_service_impl;

import com.codegym.project_module_5.model.restaurant_model.Category;
import com.codegym.project_module_5.repository.restaurant_repository.ICategoryRepository;
import com.codegym.project_module_5.service.restaurant_service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    ICategoryRepository categoryRepository;

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }
}
