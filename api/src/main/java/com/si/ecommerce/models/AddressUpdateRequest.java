package com.si.ecommerce.models;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressUpdateRequest {
    @NotBlank(message = "Please provide street name")
    private String street;

    @NotBlank(message = "Please provide city name")
    private String city;

    @NotBlank(message = "Please provide state name")
    private String state;

    @NotBlank(message = "Please provide city zipcode")
    private String zipcode;

    @NotBlank(message = "Please provide country name")
    private String country;
}
