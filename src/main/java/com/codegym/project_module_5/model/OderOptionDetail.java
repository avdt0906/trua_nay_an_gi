package com.codegym.project_module_5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OderOptionDetail {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_detail_id")
    @NotNull
    private OrderDetail orderDetail;
    @ManyToOne
    @JoinColumn(name = "dish_option_id")
    @NotNull
    private DishOption dishOption;
}
