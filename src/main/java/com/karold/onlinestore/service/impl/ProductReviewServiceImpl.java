package com.karold.onlinestore.service.impl;

import com.karold.onlinestore.exception.ProductNotFoundException;
import com.karold.onlinestore.exception.ProductReviewCannotBeEdited;
import com.karold.onlinestore.exception.ResourceNotFoundException;
import com.karold.onlinestore.model.Product;
import com.karold.onlinestore.model.ProductReview;
import com.karold.onlinestore.model.User;
import com.karold.onlinestore.payload.ProductReviewRequest;
import com.karold.onlinestore.repository.ProductRepository;
import com.karold.onlinestore.repository.ProductReviewRepository;
import com.karold.onlinestore.repository.UserRepository;
import com.karold.onlinestore.service.ProductReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    private ProductReviewRepository productReviewRepository;

    public ProductReviewServiceImpl(ProductRepository productRepository, UserRepository userRepository, ProductReviewRepository productReviewRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productReviewRepository = productReviewRepository;
    }

    @Override
    public ProductReview addProductReview(String email, ProductReviewRequest review) {
        User author = userRepository.getByEmail(email);

        Product product = productRepository.findById(review.getProductId()).orElseThrow(() -> {
            throw new ProductNotFoundException(review.getProductId());
        });

        return productReviewRepository.save(new ProductReview(author, product, review.getTitle(), review.getContent()));
    }

    @Override
    public List<ProductReview> getReviewsForProduct(Long productId) {
        return productReviewRepository.findAllByProduct_Id(productId);
    }

    @Override
    public ProductReview editProductReview(String email, Long reviewId, ProductReviewRequest updatedReview) {
        ProductReview review = productReviewRepository.findById(reviewId).orElseThrow(() -> {
            throw new ResourceNotFoundException("ProductReview", "id", reviewId);
        });
        User author = userRepository.getByEmail(email);
        if (checkIfUserIsAuthor(review, author)) {
            review.update(updatedReview);
            return productReviewRepository.save(review);
        }
        throw new ProductReviewCannotBeEdited(reviewId);
    }

    @Override
    public void deleteProductReview(String email, Long reviewId) {
        ProductReview review = productReviewRepository.findById(reviewId).orElseThrow(() -> {
            throw new ResourceNotFoundException("ProductReview", "id", reviewId);
        });
        User author = userRepository.getByEmail(email);
        if (checkIfUserIsAuthor(review, author)) {
            productReviewRepository.deleteById(reviewId);
        }
        throw new ProductReviewCannotBeEdited(reviewId);
    }

    private boolean checkIfUserIsAuthor(ProductReview review, User author) {
        return review.getAuthor().equals(author);
    }
}
