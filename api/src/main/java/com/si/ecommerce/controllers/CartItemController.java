package com.si.ecommerce.controllers;

import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.exceptions.UnauthorizeRequestException;
import com.si.ecommerce.models.CartItemRequest;
import com.si.ecommerce.models.CartResponse;
import com.si.ecommerce.models.CartUpdateRequest;
import com.si.ecommerce.services.AuthenticationService;
import com.si.ecommerce.services.CartItemService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CartItemController {
    private final CartItemService cartItemService;

    private final AuthenticationService authenticationService;

    @PostMapping("/users/{userId}/cart/cartItems")
    public CartResponse createCartItem(@PathVariable int userId, @Valid @RequestBody CartItemRequest cartItemRequest)
            throws NotFoundException, UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        return cartItemService.createCartItem(userId, cartItemRequest);
    }

    @PatchMapping("/users/{userId}/cart/cartItems/{cartItemId}")
    public void updateCartItem(@PathVariable int userId, @PathVariable int cartItemId, @Valid @RequestBody CartUpdateRequest cartUpdateRequest)
            throws NotFoundException, UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        cartItemService.updateCartItem(cartItemId, cartUpdateRequest);
    }

    @DeleteMapping("/users/{userId}/cart/cartItems/{cartItemId}")
    public void deleteCartItem(@PathVariable int userId, @PathVariable int cartItemId) throws NotFoundException
            , UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        cartItemService.deleteCartItem(cartItemId);
    }
}