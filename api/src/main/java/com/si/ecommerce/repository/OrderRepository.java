package com.si.ecommerce.repository;

import com.si.ecommerce.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findAllByUserId(int userId);
}
