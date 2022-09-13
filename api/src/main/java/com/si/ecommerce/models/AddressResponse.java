package com.si.ecommerce.models;

import com.si.ecommerce.domain.Address;
import com.si.ecommerce.domain.CartItem;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressResponse {
    private int addressId;

    private String street;

    private String city;

    private String state;

    private String zipcode;

    private String country;

    public static AddressResponse from(Address address) {
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setAddressId(address.getId());
        addressResponse.setCity(address.getCity());
        addressResponse.setStreet(address.getStreet());
        addressResponse.setZipcode(address.getZipcode());
        addressResponse.setState(address.getState());
        addressResponse.setCountry(address.getCountry());
        return addressResponse;
    }
}
