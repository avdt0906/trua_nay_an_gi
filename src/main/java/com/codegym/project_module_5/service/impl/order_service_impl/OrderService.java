package com.codegym.project_module_5.service.impl.order_service_impl;

import com.codegym.project_module_5.model.order_model.OrderStatus;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.repository.order_repository.IOrderRepository;
import com.codegym.project_module_5.repository.order_repository.IOrderStatusRepository;
import com.codegym.project_module_5.service.order_service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderStatusRepository orderStatusRepository; // Cần thêm repository này

    @Override
    public Iterable<Orders> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Orders> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void save(Orders order) {
        orderRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public long count() {
        return orderRepository.count();
    }

    @Override
    public Iterable<Orders> findAllByRestaurantId(Long restaurantId) {
        return orderRepository.findAllByRestaurantId(restaurantId);
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        Optional<Orders> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();
            // Giả sử chỉ có thể hủy đơn hàng khi đang ở trạng thái "Đang chờ" (ID = 1)
            if (order.getOrderStatus().getId() == 1) {
                OrderStatus cancelledStatus = orderStatusRepository.findById(4L) // Giả sử ID 4 là "Đã hủy"
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái Đã hủy"));
                order.setOrderStatus(cancelledStatus);
                orderRepository.save(order);
                return true;
            }
        }
        return false;
    }
}
