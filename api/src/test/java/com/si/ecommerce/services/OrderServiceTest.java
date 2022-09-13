package com.si.ecommerce.services;

import com.si.ecommerce.domain.Address;
import com.si.ecommerce.domain.Cart;
import com.si.ecommerce.domain.CartItem;
import com.si.ecommerce.domain.Product;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.exceptions.BadRequestException;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.OrderRequest;
import com.si.ecommerce.models.OrderResponse;
import com.si.ecommerce.repository.AddressRepository;
import com.si.ecommerce.repository.CartItemRepository;
import com.si.ecommerce.repository.CartRepository;
import com.si.ecommerce.repository.OrderItemRepository;
import com.si.ecommerce.repository.OrderRepository;
import com.si.ecommerce.repository.ProductRepository;
import com.si.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderServiceTest {
    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final AddressRepository addressRepository;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderService orderService;

    private User user;

    private Cart cart;

    private Product product;

    private CartItem cartItem;

    private Address address;

    private OrderRequest orderRequest;

    @BeforeEach
    public void setUp() {
        user = new User("dp@1000","password","d","p", "52437624", User.UserType.CUSTOMER);
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

        address = new Address();
        address.setUser(user);
        address.setStreet("MG road");
        address.setCity("NSK");
        address.setCountry("India");
        address.setState("MH");
        address.setZipcode("234567");
        addressRepository.save(address);

        orderRequest = new OrderRequest();
        orderRequest.setUserId(user.getId());
        orderRequest.setAddressId(address.getId());
        orderRequest.setCartId(cart.getId());
    }

    @AfterEach
    public void clearDB() {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateOrder_Success() throws NotFoundException, BadRequestException {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        assertEquals(cartItem.getProduct().getName(), orderResponse.getOrderItemResponseList().get(0).getName());
    }

    @Test
    public void testCreateOrder_Failure_CartNotFound() throws BadRequestException {
        orderRequest.setCartId(0);
        try {
            orderService.createOrder(orderRequest);
        } catch(NotFoundException exception) {
            assertEquals("Cart not found", exception.getMessage());
        }
    }

    @Test
    public void testCreateOrder_Failure_AddressNotFound() throws BadRequestException {
        orderRequest.setAddressId(0);
        try {
            orderService.createOrder(orderRequest);
        } catch(NotFoundException exception) {
            assertEquals("Address not found", exception.getMessage());
        }
    }

    @Test
    public void testCreateOrder_Failure_UserDetailsNotFound() throws BadRequestException {
        orderRequest.setUserId(0);
        try {
            orderService.createOrder(orderRequest);
        } catch(NotFoundException exception) {
            assertEquals("User details not found", exception.getMessage());
        }
    }

    @Test
    public void testCreateOrder_Failure_CartItemNotFound() throws BadRequestException {
        cartItem = null;
        try {
            orderService.createOrder(orderRequest);
        } catch(NotFoundException exception) {
            assertEquals("Please add products to create order", exception.getMessage());
        }
    }

    @Test
    public void testCreateOrder_Failure_InsufficientQuantity() throws BadRequestException {
        cartItem.setQuantity(100);
        try {
            orderService.createOrder(orderRequest);
        } catch(NotFoundException exception) {
            assertEquals("Insufficient product quantity", exception.getMessage());
        }
    }

    @Test
    public void testGetAllOrder_Success() throws NotFoundException, BadRequestException {
        orderService.createOrder(orderRequest);
        List<OrderResponse> orderResponseList = orderService.getAllOrder(user.getId());
        assertEquals(cartItem.getProduct().getName(), orderResponseList.get(0).getOrderItemResponseList().get(0).getName());
    }

    @Test
    public void testGetAllOrder_Failure() {
        try {
            orderService.getAllOrder(0);
        } catch(NotFoundException exception) {
            assertEquals("User details not found", exception.getMessage());
        }
    }
}
