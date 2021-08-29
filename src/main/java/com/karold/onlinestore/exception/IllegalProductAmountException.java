package com.karold.onlinestore.exception;

public class IllegalProductAmountException extends RuntimeException {

    public IllegalProductAmountException(int amount) {
        super("Illegal product amount: " + amount + " given.");
    }
}
