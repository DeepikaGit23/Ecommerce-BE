package com.si.ecommerce.controllers;

import com.si.ecommerce.domain.User;
import com.si.ecommerce.models.UserLoginRequest;
import com.si.ecommerce.repository.CartRepository;
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
import static org.junit.Assert.assertNotNull;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ControllerTestUtil controllerTestUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    private User user;

    @Before
    public void setUp() {
        user = new User("dp@500", "password", "d", "p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user);
    }

    @After
    public void clearDB() {
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetToken_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                 .contentType(MediaType.APPLICATION_JSON_VALUE)
                 .content(controllerTestUtil.modelToJson(new UserLoginRequest(user.getEmail(), user.getPassword()))))
                 .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertNotNull(mvcResult.getResponse());
    }

    @Test
    public void testGetToken_Failure() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                 .contentType(MediaType.APPLICATION_JSON_VALUE)
                 .content(controllerTestUtil.modelToJson(new UserLoginRequest("abc@1", "abc@1"))))
                 .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }
}