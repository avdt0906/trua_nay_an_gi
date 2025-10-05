package com.codegym.project_module_5.repository.order_repository;

import com.codegym.project_module_5.model.dto.sales.OrderStatisticDto;
import com.codegym.project_module_5.model.order_model.OrderStatus;
import com.codegym.project_module_5.model.order_model.OrderDetail;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.user_model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByRestaurantId(Long restaurantId);
    List<Orders> findAllByUserOrderByCreatedAtDesc(User user);
  
//    List<Orders> findAllByOrderId(Long orderId, Long userId);
    Page<Orders> findAllByOrderStatus_Id(Long statusId, Pageable pageable);

     @Query("SELECT SUM(o.totalPrice) FROM Orders o " +
           "WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :start AND :end")
    Double sumTotalPriceByRestaurantAndCreatedAtBetween(
            @Param("restaurantId") Long restaurantId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


    @Query(value = """
    select year(created_at)   as year,
           month(created_at)  as month,
           quarter(created_at) as quarter,
           sum(total_price)   as totalSales,
           count(id)          as totalOrders
    from orders
    group by year(created_at), month(created_at), quarter(created_at)
    order by year(created_at), month(created_at), quarter(created_at)
    """, nativeQuery = true)
    List<OrderStatisticDto> getOrdersByMonth();
}
