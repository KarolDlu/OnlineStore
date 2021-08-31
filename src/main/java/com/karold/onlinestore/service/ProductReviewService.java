package com.karold.onlinestore.service;

import com.karold.onlinestore.model.ProductReview;
import com.karold.onlinestore.payload.ProductReviewRequest;

import java.util.List;

public interface ProductReviewService {

    ProductReview addProductReview(String email, ProductReviewRequest review);

    List<ProductReview> getReviewsForProduct(Long productId);

    ProductReview editProductReview(String email, Long reviewId, ProductReviewRequest updatedReview);

    void deleteProductReview(String email, Long reviewId);
}
