package com.si.ecommerce.services;

import com.si.ecommerce.domain.Cart;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.UserUpdateRequest;
import com.si.ecommerce.models.UserRequest;
import com.si.ecommerce.models.UserResponse;
import com.si.ecommerce.repository.CartRepository;
import com.si.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService {
    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    public UserResponse createUser(UserRequest userRequest) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
        if (optionalUser.isPresent()) {
            throw new NotFoundException("User with the email id: " + userRequest.getEmail() + " already exists");
        }

        User user = new User(userRequest.getEmail(), new BCryptPasswordEncoder().encode(userRequest.getPassword())
                , userRequest.getFirstName(), userRequest.getLastName(), userRequest.getPhone(), User.UserType.CUSTOMER);
        userRepository.save(user);

        if (user.getUserType().equals(User.UserType.CUSTOMER)) {
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        return UserResponse.from(user);
    }

    public UserResponse getUserDetails (String email) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User not found with email id: " + email));

        return UserResponse.from(user);
    }

    public UserResponse getUser (int userId) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User details not found"));

        return UserResponse.from(user);
    }

    public void updateUser(int userId, UserUpdateRequest userUpdateRequest) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User details not found"));
        user.setFirstName(userUpdateRequest.getFirstName());
        user.setLastName(userUpdateRequest.getLastName());
        user.setPhone(userUpdateRequest.getPhone());
        userRepository.save(user);
    }

    public void deleteUser(int userId) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User details not found"));

        if(user.getUserType().equals(User.UserType.CUSTOMER)) {
            Optional<Cart> optionalCart = cartRepository.findByUserId(user.getId());
            Cart cart = optionalCart.orElseThrow(() -> new NotFoundException("Cart details not found"));
            cartRepository.delete(cart);
        }
        userRepository.delete(user);
    }

    public String getUserEmail(int userId) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User details not found"));
        return user.getEmail();
    }
}
