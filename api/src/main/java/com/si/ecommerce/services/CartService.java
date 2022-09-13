package com.si.ecommerce.services;

import com.si.ecommerce.domain.Cart;
import com.si.ecommerce.domain.CartItem;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.CartItemResponse;
import com.si.ecommerce.models.CartResponse;
import com.si.ecommerce.repository.CartItemRepository;
import com.si.ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CartService {
    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    public CartResponse getCart(int userId) throws NotFoundException {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        Cart cart = optionalCart.orElseThrow(() -> new NotFoundException("Cart not found"));

        List<CartItem> cartItemList = cartItemRepository.findAllByCartId(cart.getId());

        List<CartItemResponse> cartItemResponseList = cartItemList.stream()
                .map(cartItem -> CartItemResponse.from(cartItem)).collect(Collectors.toList());

        return CartResponse.from(cart, cartItemResponseList);
    }
}
