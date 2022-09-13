package com.si.ecommerce.services;

import com.si.ecommerce.domain.Address;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.AddressRequest;
import com.si.ecommerce.models.AddressResponse;
import com.si.ecommerce.models.AddressUpdateRequest;
import com.si.ecommerce.repository.AddressRepository;
import com.si.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AddressServiceTest {
    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final AddressService addressService;

    private AddressRequest addressRequest;

    private User user;

    private Address address;

    @BeforeEach
    public void setUp() {
        user = new User("dp@2000","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user);

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

    @AfterEach
    public void clearDB() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateAddress_Success() throws NotFoundException {
        AddressResponse addressResponse = addressService.createAddress(addressRequest);
        assertEquals(addressRequest.getStreet(), addressResponse.getStreet());
    }

    @Test
    public void testCreateOrder_Failure_UserNotFound() {
        addressRequest.setUserId(0);
        try {
            addressService.createAddress(addressRequest);
        } catch(NotFoundException exception) {
            assertEquals("User not found", exception.getMessage());
        }
    }

    @Test
    public void testGetAddress_Success() throws NotFoundException {
        AddressResponse addressResponse = addressService.getAddress(address.getId());
        assertEquals(address.getStreet(), addressResponse.getStreet());
    }

    @Test
    public void testGetAddress_Failure_AddressNotFound() {
        try {
            addressService.getAddress(0);
        } catch(NotFoundException exception) {
            assertEquals("Address not found", exception.getMessage());
        }
    }

    @Test
    public void testGetAllAddress_Success() {
        List<AddressResponse> addressResponseList = addressService.getAllAddress(user.getId());
        assertEquals(address.getStreet(), addressResponseList.get(0).getStreet());
    }

    @Test
    public void testUpdateAddress_Success() throws NotFoundException {
        AddressUpdateRequest addressUpdateRequest = new AddressUpdateRequest("BG","BNG","KA","507432","IN");
        addressService.updateAddress(address.getId(), addressUpdateRequest);
        assertEquals(addressUpdateRequest.getStreet(), addressRepository.findById(address.getId()).get().getStreet());
    }

    @Test
    public void testDeleteAddress_Success() throws NotFoundException {
        addressService.deleteAddress(address.getId());
        assertFalse(addressRepository.findById(address.getId()).isPresent());
    }

    @Test
    public void testDeleteAddress_Failure_AddressNotFound() {
        try {
            addressService.deleteAddress(0);
        } catch(NotFoundException exception) {
            assertEquals("Address not found", exception.getMessage());
        }
    }
}
