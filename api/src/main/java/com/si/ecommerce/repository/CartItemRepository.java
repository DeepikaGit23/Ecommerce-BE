package com.si.ecommerce.repository;

import com.si.ecommerce.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    List<CartItem> findAllByCartId(int cartId);
}
