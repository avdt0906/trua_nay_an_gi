package com.codegym.project_module_5.repository.dish_option_repository;

import com.codegym.project_module_5.model.restaurant_model.DishOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface IDishOptionRepository extends JpaRepository<DishOption, Long> {
    List<DishOption> findByDish_Id(Long id);
}
