package com.shrimpshop.controller;

import com.shrimpshop.model.Product;
import org.springframework.web.bind.annotation.*;
import com.shrimpshop.service.DynamoProductService;
import com.shrimpshop.dto.UpdatePriceRequest;

import java.util.List;

@RestController

@RequestMapping("/api/products")
public class ProductController {
    private final DynamoProductService productService;

    public ProductController(DynamoProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        
        //Always creates new ID
        int nextId = productService.getNextProductId();
        String skuId = String.format("%07d", nextId);
        product.setId(skuId);

        productService.saveProduct(product);
        return product;
    }

    @PutMapping("/{id}/price")
    public void updateProductPrice(@PathVariable String id, @RequestBody UpdatePriceRequest request){
        productService.updateProductPrice(id, request.getPrice());
    }
}
