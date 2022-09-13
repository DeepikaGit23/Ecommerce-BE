package com.si.ecommerce.services;

import com.si.ecommerce.domain.Product;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.ProductResponse;
import com.si.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductServiceTest {
    private final ProductService productService;

    private final ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(500, "Laptop", 5000.0, "WFH Setup", 5);
        productRepository.save(product);
    }

    @AfterEach
    public void clearDB() {
        productRepository.deleteAll();
    }

    @Test
    public void testGetAllProducts_Success() {
        List<ProductResponse> productList = productService.getAllProducts();

        assertTrue(productList.size() > 0);
        assertEquals(productList.get(0).getClass(), ProductResponse.class);
    }

    @Test
    public void testGetProduct_Success() throws NotFoundException {
        assertEquals(product.getName(), productService.getProduct(500).getName());
    }

    @Test
    public void testGetProduct_Failure_ProductDetailsNotFound() {
        try {
            productService.getProduct(0);
        } catch (NotFoundException exception) {
            assertEquals("Product details not found", exception.getMessage());
        }
    }
}
