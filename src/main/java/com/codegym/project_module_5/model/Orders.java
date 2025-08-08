package com.codegym.project_module_5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "shipper_id")
    private Shipper shipper;
    @ManyToOne
    @JoinColumn(name = "order_status_id")
    @NotNull
    private OrderStatus orderStatus;
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
    private String customerNote;
}
