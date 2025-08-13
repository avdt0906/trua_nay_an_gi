package com.codegym.project_module_5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private Restaurant restaurant;
    @NotNull
    private Double price;
    private String description;
    private String pictureUrl;
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
    private Boolean isAvailable = true;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
