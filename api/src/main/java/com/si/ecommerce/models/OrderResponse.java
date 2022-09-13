package com.si.ecommerce.models;

import com.si.ecommerce.domain.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderResponse {
    private int id;

    private int userId;

    private AddressResponse addressResponse;

    private LocalDateTime orderDate;

    private Order.OrderStatus orderStatus;

    private List<OrderItemResponse> orderItemResponseList;

    private double totalAmount;

    public static OrderResponse from(Order order, List<OrderItemResponse> orderItemResponseList) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setUserId(order.getUser().getId());
        orderResponse.setAddressResponse(AddressResponse.from(order.getAddress()));
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setOrderItemResponseList(orderItemResponseList);
        orderResponse.setTotalAmount(calculateTotalAmount(orderItemResponseList));
        return orderResponse;
    }

    private static double calculateTotalAmount(List<OrderItemResponse> orderItemResponseList) {
        double totalAmount = 0.0;
        for (OrderItemResponse orderItemResponse : orderItemResponseList) {
            totalAmount += orderItemResponse.getPurchasePrice() * orderItemResponse.getQuantity();
        }

        return totalAmount;
    }
}
