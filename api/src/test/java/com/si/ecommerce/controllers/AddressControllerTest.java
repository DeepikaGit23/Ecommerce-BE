package com.si.ecommerce.controllers;

import com.si.ecommerce.domain.Address;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.AddressRequest;
import com.si.ecommerce.models.AddressResponse;
import com.si.ecommerce.models.AddressUpdateRequest;
import com.si.ecommerce.models.CartItemRequest;
import com.si.ecommerce.models.CartResponse;
import com.si.ecommerce.repository.AddressRepository;
import com.si.ecommerce.repository.UserRepository;
import com.si.ecommerce.utils.ControllerTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import static org.junit.Assert.assertFalse;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ControllerTestUtil controllerTestUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private User user;

    private User user1;

    private Address address;

    private AddressRequest addressRequest;

    @Before
    public void setUp() {
        user = new User("dp@2000","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user);

        user1 = new User("dp@2001","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user1);

        address = new Address();
        address.setUser(user);
        address.setStreet("MG Road");
        address.setCity("NSK");
        address.setCountry("India");
        address.setState("MH");
        address.setZipcode("234567");
        addressRepository.save(address);

        addressRequest = new AddressRequest();
        addressRequest.setUserId(user.getId());
        addressRequest.setStreet("RJ Road");
        addressRequest.setCity("NSK");
        addressRequest.setState("MH");
        addressRequest.setZipcode("123456");
        addressRequest.setCountry("IN");
    }

    @After
    public void clearDB() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateAddress_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/" + user.getId() + "/addresses")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(addressRequest))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        AddressResponse addressResponse = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString()
                , AddressResponse.class);
        assertEquals(addressRequest.getStreet(), addressResponse.getStreet());
    }

    @Test
    public void testCreateAddress_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/" + user1.getId() + "/addresses")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(addressRequest))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetAddress_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/addresses/" + address.getId())
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        AddressResponse addressResponse = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString()
                , AddressResponse.class);
        assertEquals(address.getStreet(), addressResponse.getStreet());
    }

    @Test
    public void testGetAddress_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user1.getId() + "/addresses/" + address.getId())
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetAllAddress_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/addresses")
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        AddressResponse[] addressResponses = controllerTestUtil.jsonToModel(mvcResult.getResponse().getContentAsString()
                , AddressResponse[].class);
        assertEquals(address.getStreet(), addressResponses[0].getStreet());
    }

    @Test
    public void testGetAllAddress_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user1.getId() + "/addresses")
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateAddress_Success() throws Exception {
        AddressUpdateRequest addressUpdateRequest = new AddressUpdateRequest("BG", "BNG", "KA", "507432", "IN");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + user.getId() + "/addresses/"
                 + address.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(addressUpdateRequest))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(addressUpdateRequest.getStreet(), addressRepository.findById(address.getId()).get().getStreet());
    }

    @Test
    public void testUpdateAddress_Failure_Authorization() throws Exception {
        AddressUpdateRequest addressUpdateRequest = new AddressUpdateRequest("BG", "BNG", "KA", "507432", "IN");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + user1.getId() + "/addresses/"
                + address.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(controllerTestUtil.modelToJson(addressUpdateRequest))
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDeleteAddress_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId() + "/addresses/"
                 + address.getId())
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertFalse(addressRepository.findById(address.getId()).isPresent());
    }

    @Test
    public void testDeleteAddress_Failure_Authorization() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user1.getId() + "/addresses/"
                 + address.getId())
                .header("Authorization", controllerTestUtil.generateToken(user)))
                .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus());
    }
}
