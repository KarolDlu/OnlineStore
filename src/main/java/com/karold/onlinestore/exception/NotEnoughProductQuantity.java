package com.karold.onlinestore.exception;

public class NotEnoughProductQuantity extends RuntimeException {

    public NotEnoughProductQuantity(int available, int needed) {
        super("Not enough product quantity. Available: " + available+", needed: " + needed);
    }
}
