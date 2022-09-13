package com.si.ecommerce.controllers;

import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.exceptions.UnauthorizeRequestException;
import com.si.ecommerce.models.AddressRequest;
import com.si.ecommerce.models.AddressResponse;
import com.si.ecommerce.models.AddressUpdateRequest;
import com.si.ecommerce.services.AddressService;
import com.si.ecommerce.services.AuthenticationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AddressController {
    private final AuthenticationService authenticationService;

    private final AddressService addressService;

    @PostMapping("/users/{userId}/addresses")
    public AddressResponse createAddress(@PathVariable int userId, @Valid @RequestBody AddressRequest addressRequest)
            throws NotFoundException, UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        return addressService.createAddress(addressRequest);
    }

    @GetMapping("/users/{userId}/addresses/{addressId}")
    public AddressResponse getAddress(@PathVariable int userId, @PathVariable int addressId) throws NotFoundException, UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        return addressService.getAddress(addressId);
    }

    @GetMapping("/users/{userId}/addresses")
    public List<AddressResponse> getAllAddress(@PathVariable int userId) throws NotFoundException
            , UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        return addressService.getAllAddress(userId);
    }

    @PatchMapping("/users/{userId}/addresses/{addressId}")
    public void updateAddress(@PathVariable int userId, @PathVariable int addressId
            , @Valid @RequestBody AddressUpdateRequest addressUpdateRequest)
            throws NotFoundException, UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        addressService.updateAddress(addressId, addressUpdateRequest);
    }

    @DeleteMapping("/users/{userId}/addresses/{addressId}")
    public void deleteAddress(@PathVariable int userId, @PathVariable int addressId) throws NotFoundException
            , UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        addressService.deleteAddress(addressId);
    }
}
