package com.si.ecommerce.controllers;

import com.si.ecommerce.domain.Product;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.models.ProductResponse;
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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ControllerTestUtil controllerTestUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private Product product;

    private User user;

    @Before
    public void setUp() {
        user = new User("dp@500","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user);

        product = new Product(500, "Laptop", 5000.0, "WFH Setup", 5);
        productRepository.save(product);
    }

    @After
    public void clearDB() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllProducts_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/products")).andReturn();

        ProductResponse[] products = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString()
                , ProductResponse[].class);

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(products.length > 0);
        assertEquals(products[0].getClass(), ProductResponse.class);
    }

    @Test
    public void testGetProduct_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/products/" + product.getId())).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(product.getName(), controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString()
                , ProductResponse.class).getName());
    }

    @Test
    public void testGetProduct_Failure() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/products/" + 0)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        assertEquals("Product details not found", mvcResult.getResolvedException().getMessage());
    }

}
