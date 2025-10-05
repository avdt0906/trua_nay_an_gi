package com.codegym.project_module_5.model.baner_model;

import com.codegym.project_module_5.model.restaurant_model.Dish;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Banner {
@Id
 @GeneratedValue (strategy = GenerationType.IDENTITY)
private Long id;
@OneToOne
@JoinColumn(name = "dish_id")
private Dish dish;
private boolean  featured;
private boolean  promotion;
}
