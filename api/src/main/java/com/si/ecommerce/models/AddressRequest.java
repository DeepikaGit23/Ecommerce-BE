package com.si.ecommerce.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;;
import lombok.Setter;

@Setter
@Getter
public class AddressRequest {
    @NotNull(message = "Please provide user Id")
    @Min(value = 1, message = "Please provide user Id")
    private int userId;

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
