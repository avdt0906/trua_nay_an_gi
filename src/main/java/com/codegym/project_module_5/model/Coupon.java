package com.codegym.project_module_5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private Restaurant restaurant;
    private Double fixedDiscount;
    private Integer percentDiscount;
    private Double minOrder;
    private Double maxDiscount;
    private String description;
    private Boolean isAvailable = true;
}
