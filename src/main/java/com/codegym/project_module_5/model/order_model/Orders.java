package com.codegym.project_module_5.model.order_model;

import com.codegym.project_module_5.model.restaurant_model.Coupon;
import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.model.shipper_model.Shipper;
import com.codegym.project_module_5.model.user_model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @NotNull
    private String address;
    @NotNull
    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
