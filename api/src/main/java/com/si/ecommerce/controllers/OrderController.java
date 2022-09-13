package com.si.ecommerce.controllers;

import com.si.ecommerce.exceptions.BadRequestException;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.exceptions.UnauthorizeRequestException;
import com.si.ecommerce.models.AddressRequest;
import com.si.ecommerce.models.AddressResponse;
import com.si.ecommerce.models.OrderRequest;
import com.si.ecommerce.models.OrderResponse;
import com.si.ecommerce.services.AuthenticationService;
import com.si.ecommerce.services.OrderService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderController {
    private final AuthenticationService authenticationService;

    private final OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public OrderResponse createOrder(@PathVariable int userId, @Valid @RequestBody OrderRequest orderRequest)
            throws NotFoundException, UnauthorizeRequestException, BadRequestException {
        authenticationService.authenticateUser(userId);
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/users/{userId}/orders")
    public List<OrderResponse> getAllOrder(@PathVariable int userId) throws NotFoundException
            , UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        return orderService.getAllOrder(userId);
    }
}
