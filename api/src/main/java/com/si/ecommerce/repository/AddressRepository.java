package com.si.ecommerce.repository;

import com.si.ecommerce.domain.Address;
import com.si.ecommerce.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Integer> {
    List<Address> findAllByUserId(int userId);
}
