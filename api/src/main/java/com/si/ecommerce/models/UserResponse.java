package com.si.ecommerce.models;

import com.si.ecommerce.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private int id;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private User.UserType userType;

    public static UserResponse from(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setUserType(user.getUserType());
        return userResponse;
    }
}
