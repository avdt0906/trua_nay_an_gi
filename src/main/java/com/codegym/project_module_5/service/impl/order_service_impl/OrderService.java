package com.codegym.project_module_5.service.impl.order_service_impl;

import com.codegym.project_module_5.model.order_model.OrderStatus;
import com.codegym.project_module_5.model.order_model.Orders;
import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.repository.order_repository.IOrderRepository;
import com.codegym.project_module_5.repository.order_repository.IOrderStatusRepository;
import com.codegym.project_module_5.service.order_service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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


    @Override
    public List<Orders> findOrdersByUser(User user) {
        return orderRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    //thay đổi trạng thái order status
    @Override
    public void updateOrderStatus(Long orderId) {
        // Tìm đơn hàng trong DB, nếu không có thì không làm gì cả.
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            String currentStatus = order.getOrderStatus().getName();

            // Nếu trạng thái là "Chưa xác nhận", chuyển sang "Đã xác nhận"
            if ("Chưa xác nhận".equals(currentStatus)) {
                OrderStatus confirmedStatus = orderStatusRepository.findByName("Đã xác nhận")
                        .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy trạng thái 'Đã xác nhận'."));
                order.setOrderStatus(confirmedStatus);
            }
            // Nếu trạng thái là "Đã xác nhận", chuyển sang "Đang chế biến"
            else if ("Đã xác nhận".equals(currentStatus)) {
                OrderStatus preparingStatus = orderStatusRepository.findByName("Đang chế biến")
                        .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy trạng thái 'Đang chế biến'."));
                order.setOrderStatus(preparingStatus);
            }
            // Lưu lại sự thay đổi vào DB
            orderRepository.save(order);
        }
    }

}
