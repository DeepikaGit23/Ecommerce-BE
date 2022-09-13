package com.si.ecommerce.controllers;

import com.si.ecommerce.domain.User;
import com.si.ecommerce.models.UserUpdateRequest;
import com.si.ecommerce.models.UserLoginRequest;
import com.si.ecommerce.models.UserRequest;
import com.si.ecommerce.models.UserResponse;
import com.si.ecommerce.repository.CartRepository;
import com.si.ecommerce.repository.UserRepository;
import com.si.ecommerce.services.UserService;
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

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ControllerTestUtil controllerTestUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    private User user;

    private User user1;

    @Before
    public void setUp() {
        user = new User("dp@500","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user);

        user1 = new User("dp@505","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user1);
    }

    @After
    public void clearDB() {
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser_Success() throws Exception {
        UserRequest userRequest = new UserRequest("dp@501","password","d","p", "52437624");

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(userRequest)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        UserResponse userResponse = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString(), UserResponse.class);
        assertEquals(userRequest.getEmail(), userResponse.getEmail());
    }

    @Test
    public void testCreateUser_Failure() throws Exception {
        UserRequest userRequest = new UserRequest("dp@500", "password", "d", "p", "52437624");

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(userRequest)))
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testLogin_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(new UserLoginRequest(user.getEmail(), user.getPassword())))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        UserResponse userResponse = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString()
                , UserResponse.class);
        assertEquals(userResponse.getEmail(), user.getEmail());
    }

    @Test
    public void testLogin_Failure_Authentication() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(new UserLoginRequest(user.getEmail(), user.getPassword())))
                .header("Authorization", controllerTestUtil.BEARER))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetUser_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(user.getEmail(), controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString()
                , UserResponse.class).getEmail());
    }

    @Test
    public void testGetUser_Failure_Authentication() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", controllerTestUtil.BEARER))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetUser_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user1.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("de","pe", "1234567890");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + user.getId())
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .content(controllerTestUtil.modelToJson(userUpdateRequest))
               .header("Authorization", controllerTestUtil.generateToken(user)))
               .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(userUpdateRequest.getFirstName(), userService.getUser(user.getId()).getFirstName());
    }

    @Test
    public void testUpdateUser_Failure_Authentication() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + user.getId())
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .content(controllerTestUtil.modelToJson(new UserUpdateRequest("de","pe", "1234567890")))
               .header("Authorization", controllerTestUtil.generateToken(user)))
               .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateUser_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + user1.getId())
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .content(controllerTestUtil.modelToJson(new UserUpdateRequest("de","pe", "1234567890")))
               .header("Authorization", controllerTestUtil.generateToken(user)))
               .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        UserResponse userResponse = userService.createUser(new UserRequest("dp@501","password","d","p", "52437624"));
        Optional<User> user = userRepository.findById(userResponse.getId());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userResponse.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", controllerTestUtil.generateToken(user.get())))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDeleteUser_Failure_Authentication() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", controllerTestUtil.BEARER))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDeleteUser_Failure_Authorization() throws Exception {
        UserResponse userResponse = userService.createUser(new UserRequest("dp@501","password","d","p", "52437624"));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userResponse.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", controllerTestUtil.generateToken(user1)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }
}
