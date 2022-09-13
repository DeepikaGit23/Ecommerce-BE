package com.si.ecommerce.controllers;

import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.exceptions.UnauthorizeRequestException;
import com.si.ecommerce.models.UserUpdateRequest;
import com.si.ecommerce.models.UserLoginRequest;
import com.si.ecommerce.models.UserRequest;
import com.si.ecommerce.models.UserResponse;
import com.si.ecommerce.services.AuthenticationService;
import com.si.ecommerce.services.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/users")
    public UserResponse createUser(@Valid @RequestBody UserRequest userRequest) throws NotFoundException {
        return userService.createUser(userRequest);
    }

    @PostMapping("/login")
    public UserResponse loginUser(@Valid @RequestBody UserLoginRequest loginRequest) throws NotFoundException {
        authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken( loginRequest.getEmail(), loginRequest.getPassword()));
        return userService.getUserDetails(loginRequest.getEmail());
    }

    @GetMapping("/users/{userId}")
    public UserResponse getUser(@PathVariable int userId) throws NotFoundException, UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        return userService.getUser(userId);
    }

    @PatchMapping("/users/{userId}")
    public void updateUser(@PathVariable int userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest)
            throws NotFoundException, UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        userService.updateUser(userId, userUpdateRequest);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable int userId) throws NotFoundException, UnauthorizeRequestException {
        authenticationService.authenticateUser(userId);
        userService.deleteUser(userId);
    }
}
