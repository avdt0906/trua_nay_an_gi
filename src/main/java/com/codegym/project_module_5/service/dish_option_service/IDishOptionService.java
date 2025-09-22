package com.codegym.project_module_5.service.dish_option_service;

import com.codegym.project_module_5.model.restaurant_model.DishOption;
import com.codegym.project_module_5.service.general_service.IGeneralService;

import java.util.List;

public interface IDishOptionService extends IGeneralService<DishOption> {
    List<DishOption> findDishOptionByDishId(Long id);
}
