package com.si.ecommerce.controllers;

import com.si.ecommerce.domain.Address;
import com.si.ecommerce.domain.Cart;
import com.si.ecommerce.domain.CartItem;
import com.si.ecommerce.domain.Product;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.models.OrderItemResponse;
import com.si.ecommerce.models.OrderRequest;
import com.si.ecommerce.models.OrderResponse;
import com.si.ecommerce.repository.AddressRepository;
import com.si.ecommerce.repository.CartItemRepository;
import com.si.ecommerce.repository.CartRepository;
import com.si.ecommerce.repository.OrderItemRepository;
import com.si.ecommerce.repository.OrderRepository;
import com.si.ecommerce.repository.ProductRepository;
import com.si.ecommerce.repository.UserRepository;
import com.si.ecommerce.services.OrderService;
import com.si.ecommerce.utils.ControllerTestUtil;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ControllerTestUtil controllerTestUtil;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderService orderService;

    private User user;

    private User user1;

    private Cart cart;

    private Product product;

    private CartItem cartItem;

    private Address address;

    private OrderRequest orderRequest;

    @Before
    public void setUp() {
        user = new User("dp@1000","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user);

        user1 = new User("dp@1001","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user1);

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

    @After
    public void clearDB() {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateOrder_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/" + user.getId() + "/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(orderRequest))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        OrderResponse orderResponse = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString()
                , OrderResponse.class);
        assertNotNull(orderResponse.getOrderItemResponseList().get(0));
    }

    @Test
    public void testCreateOrder_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/" + user1.getId() + "/orders")
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .content(controllerTestUtil.modelToJson(orderRequest))
               .header("Authorization", controllerTestUtil.generateToken(user)))
               .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetAllOrders_Success() throws Exception {
        orderService.createOrder(orderRequest);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/orders")
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        OrderResponse[] orderResponse = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString()
                , OrderResponse[].class);
        assertEquals(OrderItemResponse.class, orderResponse[0].getOrderItemResponseList().get(0).getClass());
    }

    @Test
    public void testGetAllOrders_Failure_Authentication() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user1.getId() + "/orders")
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }
}
