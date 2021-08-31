package com.karold.onlinestore.exception;

public class ProductReviewCannotBeEdited extends RuntimeException {
    public ProductReviewCannotBeEdited(Long value) {
        super("You cannot edit review with id: " + value + ", cause: Only author can edit review.");
    }
}
