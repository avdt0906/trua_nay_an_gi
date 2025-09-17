package com.codegym.project_module_5.model.evaluate_model;

import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.user_model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Evaluate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String comment;
    @NotNull
    private int star;
    @NotNull
    private String response_rate;
    @NotNull
    private String response_time;
    @NotNull
    private LocalDateTime followers;
    @NotNull
    private String outstanding_features;
    @NotNull
    private String product_quality;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
