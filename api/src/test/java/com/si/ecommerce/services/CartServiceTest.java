package com.si.ecommerce.services;

import com.si.ecommerce.domain.Cart;
import com.si.ecommerce.domain.CartItem;
import com.si.ecommerce.domain.Product;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.exceptions.NotFoundException;
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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CartServiceTest {
    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final CartService cartService;

    private User user;

    private Product product;

    private CartItem cartItem;

    private Cart cart;

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
    }

    @AfterEach
    public void clearDB() {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetCart_Success() throws NotFoundException {
        assertTrue(cartService.getCart(user.getId()).getCartItems().size() > 0);
        assertEquals(product.getName(), cartService.getCart(user.getId()).getCartItems().get(0).getName());
    }

    @Test
    public void testGetCart_Failure_CartNotFound() {
        try {
            cartService.getCart(0);
        } catch (NotFoundException exception) {
            assertEquals("Cart not found", exception.getMessage());
        }
    }
}
