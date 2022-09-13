package com.si.ecommerce.repository;

import com.si.ecommerce.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findOrderItemByOrderId(int orderId);
}
