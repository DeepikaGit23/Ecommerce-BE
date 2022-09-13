package com.si.ecommerce.repository;

import com.si.ecommerce.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUserId(int id);
}
