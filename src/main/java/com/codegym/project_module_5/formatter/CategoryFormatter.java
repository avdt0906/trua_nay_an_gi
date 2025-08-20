package com.codegym.project_module_5.formatter;

import com.codegym.project_module_5.model.restaurant_model.Category;
import com.codegym.project_module_5.service.restaurant_service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

@Component
public class CategoryFormatter implements Formatter<Category> {
    @Autowired
    ICategoryService categoryService;

    @Override
    public Category parse(String text, Locale locale) throws ParseException {
        Optional<Category> category = categoryService.findById(Long.parseLong(text));
        return category.get();
    }

    @Override
    public String print(Category object, Locale locale) {
        return object.getName();
    }
}
