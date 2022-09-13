package com.si.ecommerce.models;

import com.si.ecommerce.domain.CartItem;
import lombok.Getter;

import lombok.Setter;

@Setter
@Getter
public class CartItemResponse {

    private int id;

    private int productId;

    private String name;

    private double price;

    private String description;

    private int quantity;

    public static CartItemResponse from(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setId(cartItem.getId());
        cartItemResponse.setProductId(cartItem.getProduct().getId());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setPrice(cartItem.getProduct().getPrice());
        cartItemResponse.setDescription(cartItem.getProduct().getDescription());
        cartItemResponse.setName(cartItem.getProduct().getName());

        return cartItemResponse;
    }
}
