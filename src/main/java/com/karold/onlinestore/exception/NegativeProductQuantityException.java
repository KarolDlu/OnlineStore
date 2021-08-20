package com.karold.onlinestore.exception;

public class NegativeProductQuantityException extends RuntimeException {
    public NegativeProductQuantityException() {
        super("Negative quantity of product was given.");
    }
}
