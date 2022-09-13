package com.si.ecommerce.controllers;

import com.si.ecommerce.domain.Cart;
import com.si.ecommerce.domain.CartItem;
import com.si.ecommerce.domain.Product;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.models.CartResponse;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {
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
    }

    @After
    public void clearDB() {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetCart_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/cart")
               .header("Authorization", controllerTestUtil.generateToken(user)))
               .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        CartResponse cartResponse = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString()
                , CartResponse.class);
        assertEquals(product.getName(), cartResponse.getCartItems().get(0).getName());
    }

    @Test
    public void testGetCart_Failure_Authentication() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/cart")
               .header("Authorization", controllerTestUtil.BEARER))
               .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetCart_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user1.getId() + "/cart")
                        .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }
}
