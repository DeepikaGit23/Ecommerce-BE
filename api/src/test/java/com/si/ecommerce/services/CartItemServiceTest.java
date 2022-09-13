package com.si.ecommerce.services;

import com.si.ecommerce.domain.Cart;
import com.si.ecommerce.domain.CartItem;
import com.si.ecommerce.domain.Product;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.CartItemRequest;
import com.si.ecommerce.models.CartResponse;
import com.si.ecommerce.models.CartUpdateRequest;
import com.si.ecommerce.repository.CartItemRepository;
import com.si.ecommerce.repository.CartRepository;
import com.si.ecommerce.repository.ProductRepository;
import com.si.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CartItemServiceTest {
    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final CartService cartService;

    private final CartItemService cartItemService;

    private User user;

    private Product product;

    private CartItem cartItem;

    private Cart cart;

    private CartUpdateRequest cartUpdateRequest;

    @BeforeEach
    public void setUp() {
        user = new User("dp@501","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user);

        cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        product = new Product(500, "Laptop", 5000.0, "WFH Setup", 5);
        productRepository.save(product);

        cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setQuantity(1);
        cartItem.setProduct(product);
        cartItemRepository.save(cartItem);

        cartUpdateRequest = new CartUpdateRequest();
        cartUpdateRequest.setQuantity(3);
    }

    @AfterEach
    public void clearDB() {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateCartItem_Success() throws NotFoundException {
        product = new Product(501, "Desktop", 50000.0, "WFH Setup", 5);
        productRepository.save(product);

        CartResponse cartResponse = cartItemService.createCartItem(cart.getId(),new CartItemRequest(501,2));
        assertEquals(product.getName(), cartResponse.getCartItems().get(1).getName());
    }

    @Test
    public void testCreateCartItem_Success_UpdateQuantityIfExists() throws NotFoundException {
        CartResponse cartResponse = cartItemService.createCartItem(cart.getId(),new CartItemRequest(500,2));
        assertEquals(cartItem.getQuantity() + 2, cartResponse.getCartItems().get(0).getQuantity());
    }

    @Test
    public void testCreateCartItem_Failure_CartNotFound() {
        try {
            cartItemService.createCartItem(3, new CartItemRequest(500,2));
        } catch (NotFoundException exception) {
            assertEquals("Cart not found", exception.getMessage());
        }
    }
    @Test
    public void testCreateCartItem_Failure_ProductNotFound() {
        try {
            cartItemService.createCartItem(cart.getId(), new CartItemRequest(1,2));
        } catch (NotFoundException exception) {
            assertEquals("Product not found", exception.getMessage());
        }
    }

    @Test
    public void testUpdateCartItem_Success() throws NotFoundException {
        cartItemService.updateCartItem(cartItem.getId(),cartUpdateRequest);
        assertEquals(3, cartService.getCart(user.getId()).getCartItems().get(0).getQuantity());
    }

    @Test
    public void testUpdateCartItem_Failure_CartItemNotFound() {
        try {
            cartItemService.updateCartItem(0,cartUpdateRequest);
        } catch (NotFoundException exception) {
            assertEquals("Cart item not found", exception.getMessage());
        }
    }

    @Test
    public void testDeleteCartItem_Success() throws NotFoundException {
        cartItemService.deleteCartItem(cartItem.getId());
        assertFalse(cartItemRepository.findById(cartItem.getId()).isPresent());
    }

    @Test
    public void testDeleteCartItem_Failure_CartItemNotFound() {
        try {
            cartItemService.deleteCartItem(0);
        } catch (NotFoundException exception) {
            assertEquals("Cart item not found", exception.getMessage());
        }
    }
}