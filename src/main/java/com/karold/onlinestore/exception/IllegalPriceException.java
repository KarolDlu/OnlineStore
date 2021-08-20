package com.karold.onlinestore.exception;

public class IllegalPriceException extends RuntimeException {

    public IllegalPriceException() {
        super("Illegal product price was given.");
    }
}
