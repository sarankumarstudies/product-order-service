package com.ss.product_service.controller;


import com.ss.product_service.entity.Product;
import com.ss.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    public ProductRepository productRepository;



    @PostMapping
    public Product addProduct (@RequestBody Product product)
    {
       return productRepository.save(product);
    }

    @GetMapping
    public List<Product> getProducts (@RequestBody Product product)
    {
        return productRepository.findAll();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById (@PathVariable Long productId)
    {
         Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not found with "+ productId));
        return ResponseEntity.ok(product);
    }



}
