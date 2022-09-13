package com.si.ecommerce.controllers;

import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.exceptions.UnauthorizeRequestException;
import com.si.ecommerce.models.CartResponse;
import com.si.ecommerce.services.AuthenticationService;
import com.si.ecommerce.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CartController {
    private final CartService cartService;

    private final AuthenticationService authenticationService;

    @GetMapping("/users/{userId}/cart")
    public CartResponse getCart(@PathVariable int userId) throws NotFoundException, UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        return cartService.getCart(userId);
    }
}
