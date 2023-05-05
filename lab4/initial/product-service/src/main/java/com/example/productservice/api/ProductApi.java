package com.example.productservice.api;

import com.example.productservice.service.Product;
import com.example.productservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductApi {

    private static final String X_PREMIUM_CUSTOMER_HEADER = "X-Premium-Customer";
    private final ProductService productService;

    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> allProducts(@RequestHeader(name = X_PREMIUM_CUSTOMER_HEADER, required = false, defaultValue = "false") boolean premium) {
        if (premium) {
            return productService.allProducts();
        } else {
            return productService.allNonPremiumProducts();
        }
    }

    @GetMapping("/{productIdentifier}")
    public ResponseEntity<Product> findProduct(@PathVariable("productIdentifier") UUID productIdentifier) {
        return productService.findOneByIdentifier(productIdentifier)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
