package com.codegym.project_module_5.model.restaurant_model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dish_options")
public class DishOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @ManyToOne
    @JoinColumn(name = "dish_id")
    @NotNull
    private Dish dish;
    @NotNull
    private Double price;
    private String description;
    private Boolean isAvailable = true;
}
