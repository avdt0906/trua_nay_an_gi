package com.codegym.project_module_5.model.order_model;

import com.codegym.project_module_5.model.restaurant_model.Dish;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @NotNull
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    @NotNull
    private Dish dish;

    @NotNull
    private Long quantity;

    @NotNull
    private Double price;
}