package com.codegym.project_module_5.model.restaurant_model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private Restaurant restaurant;
}
