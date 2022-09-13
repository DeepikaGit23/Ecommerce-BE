package com.si.ecommerce.models;

import com.si.ecommerce.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private int id;

    private String name;

    private double price;

    private String description;

    private int availableQuantity;

    public static ProductResponse from(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setDescription(product.getDescription());
        productResponse.setName(product.getName());
        productResponse.setPrice(product.getPrice());
        productResponse.setAvailableQuantity(product.getAvailableQuantity());
        return productResponse;
    }
}
