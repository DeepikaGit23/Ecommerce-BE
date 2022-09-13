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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AddressService {
    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    public AddressResponse createAddress(AddressRequest addressRequest) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(addressRequest.getUserId());
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User not found"));

        Address address = new Address();
        address.setUser(user);
        address.setCity(addressRequest.getCity());
        address.setCountry(addressRequest.getCountry());
        address.setState(addressRequest.getState());
        address.setZipcode(addressRequest.getZipcode());
        address.setStreet(addressRequest.getStreet());

        addressRepository.save(address);
        return AddressResponse.from(address);
    }

    public AddressResponse getAddress(int addressId) throws NotFoundException {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        Address address = optionalAddress.orElseThrow(() -> new NotFoundException("Address not found"));

        return AddressResponse.from(address);
    }

    public List<AddressResponse> getAllAddress(int userId) {
        List<Address> addressList = addressRepository.findAllByUserId(userId);
        List<AddressResponse> addressResponseList= addressList.stream()
                .map(address -> AddressResponse.from(address)).collect(Collectors.toList());

        return addressResponseList;
    }

    public void updateAddress(int addressId, AddressUpdateRequest addressUpdateRequest) throws NotFoundException {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        Address address = optionalAddress.orElseThrow(() -> new NotFoundException("Address not found"));

        address.setStreet(addressUpdateRequest.getStreet());
        address.setCity(addressUpdateRequest.getCity());
        address.setState(addressUpdateRequest.getState());
        address.setZipcode(addressUpdateRequest.getZipcode());
        address.setCountry(addressUpdateRequest.getCountry());
        addressRepository.save(address);
    }

    public void deleteAddress(int addressId) throws NotFoundException {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        Address address = optionalAddress.orElseThrow(() -> new NotFoundException("Address not found"));

        addressRepository.delete(address);
    }
}
