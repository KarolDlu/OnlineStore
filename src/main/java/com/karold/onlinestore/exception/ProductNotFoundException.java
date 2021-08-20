package com.karold.onlinestore.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product with given id: (" + id + ") not found.");
    }

    public ProductNotFoundException(String name) {
        super("Product with given name: ("+name+") not found.");
    }
}
