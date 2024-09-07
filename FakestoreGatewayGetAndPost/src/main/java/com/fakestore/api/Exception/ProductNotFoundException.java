package com.fakestore.api.Exception;

public class ProductNotFoundException extends RuntimeException {
	
    public ProductNotFoundException(String message) {
        super(message);
    }
    
}
