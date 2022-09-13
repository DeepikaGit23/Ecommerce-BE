package com.si.ecommerce.models;

import com.si.ecommerce.domain.Cart;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartResponse {

    private int id;

    private int userId;

    private List<CartItemResponse> cartItems;

    private double totalAmount;

    public static CartResponse from(Cart cart, List<CartItemResponse> cartItemResponseList) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cart.getId());
        cartResponse.setUserId(cart.getUser().getId());
        cartResponse.setCartItems(cartItemResponseList);
        cartResponse.setTotalAmount(calculateTotalAmount(cartItemResponseList));

        return cartResponse;
    }

    private static double calculateTotalAmount(List<CartItemResponse> cartItemResponseList) {
        double totalAmount = 0.0;
        for (CartItemResponse cartItemResponse : cartItemResponseList) {
            totalAmount += cartItemResponse.getPrice() * cartItemResponse.getQuantity();
        }

        return totalAmount;
    }
}
