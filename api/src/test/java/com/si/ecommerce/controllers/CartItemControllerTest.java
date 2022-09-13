package com.si.ecommerce.controllers;

import com.si.ecommerce.domain.Cart;
import com.si.ecommerce.domain.CartItem;
import com.si.ecommerce.domain.Product;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.models.CartItemRequest;
import com.si.ecommerce.models.CartResponse;
import com.si.ecommerce.models.CartUpdateRequest;
import com.si.ecommerce.repository.CartItemRepository;
import com.si.ecommerce.repository.CartRepository;
import com.si.ecommerce.repository.ProductRepository;
import com.si.ecommerce.repository.UserRepository;
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

import static org.junit.Assert.assertEquals;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartItemControllerTest {
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

    private User user;

    private User user1;

    private Product product;

    private CartItem cartItem;

    private Cart cart;

    private CartUpdateRequest cartUpdateRequest;

    @Before
    public void setUp() {
        user = new User("dp@501","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user);

        user1 = new User("dp@505","password","d","p", "52437624", User.UserType.CUSTOMER);
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

        cartUpdateRequest = new CartUpdateRequest();
        cartUpdateRequest.setQuantity(3);
    }

    @After
    public void clearDB() {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateCartItem_Success() throws Exception {
        product = new Product(501, "Desktop", 50000.0, "WFH Setup", 5);
        productRepository.save(product);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/" + user.getId() + "/cart/cartItems")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(new CartItemRequest(501,2)))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        CartResponse cartResponse = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString(), CartResponse.class);
        assertEquals(product.getName(), cartResponse.getCartItems().get(1).getName());
    }

    @Test
    public void testCreateCartItem_Success_UpdateQuantityIfExists() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/" + user.getId() + "/cart/cartItems")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(new CartItemRequest(500,2)))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        CartResponse cartResponse = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString(), CartResponse.class);
        assertEquals(cartItem.getQuantity() + 2, cartResponse.getCartItems().get(0).getQuantity());
    }

    @Test
    public void testCreateCartItem_Failure_Authentication() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/" + user.getId() + "/cart/cartItems")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(new CartItemRequest(500,2)))
                .header("Authorization", controllerTestUtil.BEARER))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testCreateCartItem_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/" + user1.getId() + "/cart/cartItems")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(new CartItemRequest(500,2)))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateCartItem_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + user.getId() + "/cart/cartItems/" + cartItem.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(cartUpdateRequest))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateCartItem_Failure_CartNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + user.getId() + "/cart/cartItems/" + 0)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(cartUpdateRequest))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        assertEquals("Cart item not found", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void testUpdateCartItem_Failure_Authentication() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + user.getId() + "/cart/cartItems/"
                + cartItem.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(cartUpdateRequest))
                .header("Authorization", controllerTestUtil.BEARER))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateCartItem_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + user1.getId() + "/cart/cartItems/"
                + cartItem.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(cartUpdateRequest))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDeleteCartItem_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId() + "/cart/cartItems/"
                 + cartItem.getId())
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDeleteCartItem_Failure_CartItemNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId() + "/cart/cartItems/" + 0)
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        assertEquals("Cart item not found", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void testDeleteCartItem_Failure_Authentication() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId() + "/cart/cartItems/"
                + cartItem.getId())
                .header("Authorization", controllerTestUtil.BEARER))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDeleteCartItem_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user1.getId() + "/cart/cartItems/"
                + cartItem.getId())
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }
}

