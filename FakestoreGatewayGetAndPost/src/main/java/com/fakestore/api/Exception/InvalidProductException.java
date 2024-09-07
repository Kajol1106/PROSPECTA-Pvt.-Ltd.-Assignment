package com.fakestore.api.Exception;

public class InvalidProductException extends RuntimeException {
	
    public InvalidProductException(String message) {
        super(message);
    }
    
}