package com.si.ecommerce.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderRequest {
    @NotNull(message = "Please provide user Id")
    @Min(value = 1, message = "Please provide user Id")
    private int userId;

    @NotNull(message = "Please provide address Id")
    @Min(value = 1, message = "Please provide address Id")
    private int addressId;

    @NotNull(message = "Please provide cart Id")
    @Min(value = 1, message = "Please provide cart Id")
    private int cartId;
}
