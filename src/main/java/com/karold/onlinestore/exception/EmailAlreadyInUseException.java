package com.karold.onlinestore.exception;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email) {
        super("Given email " + email + " is already in use.");
    }
}
