package com.si.ecommerce.services;

import com.si.ecommerce.domain.Product;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.ProductResponse;
import com.si.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponse> productResponseList = productList.stream()
                .map(product -> ProductResponse.from(product)).collect(Collectors.toList());

        return productResponseList;
    }

    public ProductResponse getProduct(int productId) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.orElseThrow(() -> new NotFoundException("Product details not found"));

        return ProductResponse.from(product);
    }

}
