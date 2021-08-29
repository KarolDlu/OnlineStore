package com.karold.onlinestore.controller;

import com.karold.onlinestore.model.Product;
import com.karold.onlinestore.service.ProductService;
import com.karold.onlinestore.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/get/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{productId}/quantity/{quantity}")
    public ResponseEntity<Product> updateProductQuantity(@PathVariable Long productId, @PathVariable int quantity) {
        return ResponseEntity.ok(productService.updateProductQuantity(productId, quantity));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product has been deleted.");
    }


}
