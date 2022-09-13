package com.si.ecommerce.models;

import com.si.ecommerce.domain.CartItem;
import com.si.ecommerce.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemResponse {
    private int id;

    private int productId;

    private String name;

    private double purchasePrice;

    private String description;

    private int quantity;

    public static OrderItemResponse from(OrderItem orderItem){
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        orderItemResponse.setId(orderItem.getId());
        orderItemResponse.setProductId(orderItem.getProduct().getId());
        orderItemResponse.setName(orderItem.getProduct().getName());
        orderItemResponse.setPurchasePrice(orderItem.getPrice());
        orderItemResponse.setDescription(orderItem.getProduct().getDescription());
        orderItemResponse.setQuantity(orderItem.getQuantity());

        return orderItemResponse;
    }
}
