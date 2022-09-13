package com.si.ecommerce.controllers;

import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.ProductResponse;
import com.si.ecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{productId}")
    public ProductResponse getProduct(@PathVariable int productId) throws NotFoundException {
        return productService.getProduct(productId);
    }
}
