package com.si.ecommerce.services;

import com.si.ecommerce.domain.User;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.UserUpdateRequest;
import com.si.ecommerce.models.UserRequest;
import com.si.ecommerce.models.UserResponse;
import com.si.ecommerce.repository.CartRepository;
import com.si.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("dp@500","password","d","p", "52437624", User.UserType.CUSTOMER);
        userRepository.save(user);
    }

    @AfterEach
    public void clearDB() {
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser_Success() throws NotFoundException {
        UserRequest userRequest = new UserRequest("dp@501","password","d","p", "52437624");
        assertEquals(userRequest.getEmail(), userService.createUser(userRequest).getEmail());
    }

    @Test
    public void testCreateUser_Failure_UserAlreadyExists() {
        UserRequest userRequest = new UserRequest("dp@500", "password", "d", "p", "52437624");

        try {
            userService.createUser(userRequest);
        } catch(NotFoundException exception) {
            String exceptionMessage = "User with the email id: " + userRequest.getEmail() + " already exists";
            assertEquals(exceptionMessage, exception.getMessage());
        }
    }

    @Test
    public void testGetUserDetails_Success() throws NotFoundException {
        assertEquals(user.getEmail(), userService.getUserDetails(user.getEmail()).getEmail());
    }

    @Test
    public void testGetUserDetails_Failure_UserNotFound() {
        try {
            userService.getUserDetails(user.getEmail());
        } catch(NotFoundException exception) {
            String exceptionMessage = "User not found with email id: " + user.getEmail();
            assertEquals(exceptionMessage, exception.getMessage());
        }
    }

    @Test
    public void testGetUser_Success() throws NotFoundException {
        assertEquals(user.getEmail(), userService.getUser(user.getId()).getEmail());
    }

    @Test
    public void testGetUser_Failure_UserDetailsNotFound() {
        try {
            userService.getUser(0);
        } catch(NotFoundException exception) {
            assertEquals("User details not found", exception.getMessage());
        }
    }

    @Test
    public void testUpdateUser_Success() throws NotFoundException {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("de", "p", " 123456");
        userService.updateUser(user.getId(), userUpdateRequest);

        assertEquals(userUpdateRequest.getFirstName(), userService.getUser(user.getId()).getFirstName());
    }

    @Test
    public void TestUpdateUser_Failure_UserDetailsNotFound() {
        try {
            userService.updateUser(0, new UserUpdateRequest("de", "p", " 123456"));
        } catch (NotFoundException exception) {
            assertEquals("User details not found", exception.getMessage());
        }
    }

    @Test
    public void testDeleteUser_Success() throws NotFoundException {
        UserResponse userResponse = userService.createUser(new UserRequest("dp@501","password","d","p", "52437624"));
        userService.deleteUser(userResponse.getId());
        assertFalse(userRepository.findById(userResponse.getId()).isPresent());
    }

    @Test
    public void testDeleteUser_Failure_UserDetailsNotFound() {
        try {
            userService.deleteUser(0);
        } catch(NotFoundException exception) {
            assertEquals("User details not found", exception.getMessage());
        }
    }
}
