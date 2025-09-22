package com.codegym.project_module_5.model.restaurant_model;



import com.codegym.project_module_5.model.baner_model.Banner;

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
        private Integer discount;
    //    @Lob
    //    @Column(columnDefinition="LONGBLOB")
        private String pictureUrl;
        @ManyToOne
        @JoinColumn(name = "tag_id")
        private Tag tag;
        private Boolean baner = false;
        private Boolean isAvailable = true;
        @ManyToOne
        @JoinColumn(name = "category_id")
        private Category category;
        @OneToOne(mappedBy = "dish", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Banner banner;

    }
