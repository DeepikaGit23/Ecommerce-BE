package com.si.ecommerce.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide valid email address")
    private String email;

    @NotBlank(message = "Please provide password")
    private String password;

    @NotBlank(message = "Please provide firstname")
    private String firstName;

    @NotBlank(message = "Please provide lastname")
    private String lastName;

    @NotBlank(message = "Please provide phone number")
    private String phone;
}
