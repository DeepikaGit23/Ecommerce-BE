package com.si.ecommerce.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Please provide email address")
    private String email;

    @NotBlank(message = "Please provide password")
    private String password;
}
