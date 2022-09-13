package com.si.ecommerce.models;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Please provide firstname")
    private String firstName;

    @NotBlank(message = "Please provide lastname")
    private String lastName;

    @NotBlank(message = "Please provide phone number")
    private String phone;
}
