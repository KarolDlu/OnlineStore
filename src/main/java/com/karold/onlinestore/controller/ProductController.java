package com.karold.onlinestore.controller;

import com.karold.onlinestore.model.Product;
import com.karold.onlinestore.model.ProductReview;
import com.karold.onlinestore.payload.ProductReviewRequest;
import com.karold.onlinestore.service.ProductReviewService;
import com.karold.onlinestore.service.ProductService;
import com.karold.onlinestore.service.impl.ProductReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    private ProductReviewService reviewService;

    @Autowired
    public ProductController(ProductService productService, ProductReviewServiceImpl reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
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
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product has been deleted.");
    }

    @PostMapping("/addReview")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ProductReview> addReview(@RequestBody ProductReviewRequest review, Principal principal) {
        return ResponseEntity.ok(reviewService.addProductReview(principal.getName(), review));
    }

    @PostMapping("/edit/review/{reviewId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ProductReview> editReview(@RequestBody ProductReviewRequest review, @PathVariable Long reviewId, Principal principal) {
        return ResponseEntity.ok(reviewService.editProductReview(principal.getName(), reviewId, review));
    }

    @GetMapping("/{productId}/get/reviews")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<List<ProductReview>> getReviewsForProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsForProduct(productId));
    }

    @DeleteMapping("/delete/review/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId, Principal principal) {
        reviewService.deleteProductReview(principal.getName(), reviewId);
        return ResponseEntity.ok("Success! Review has been deleted.");
    }

}
