package com.si.ecommerce.models;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    @NotNull(message = "Please provide product Id")
    @Min(value = 1, message = "Please provide valid product Id")
    private int productId;

    @NotNull(message = "Please provide quantity")
    @Min(value = 1, message = "Please provide valid quantity")
    private int quantity;
}
