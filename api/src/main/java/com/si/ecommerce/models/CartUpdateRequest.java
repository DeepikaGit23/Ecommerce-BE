package com.si.ecommerce.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartUpdateRequest {
    @NotNull(message = "Please provide quantity")
    @Min(value = 1, message = "Please provide valid quantity")
    private int quantity;
}
