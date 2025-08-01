package com.codegym.project_module_5.model;

import jakarta.persistence.*;
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
    private String name;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    private Double price;
    private String description;
    private String picture_url;
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
    private Boolean is_available = true;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
