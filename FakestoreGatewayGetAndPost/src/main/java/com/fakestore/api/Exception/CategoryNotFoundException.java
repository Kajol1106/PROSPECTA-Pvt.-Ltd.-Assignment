package com.fakestore.api.Exception;

public class CategoryNotFoundException extends RuntimeException {
	
    public CategoryNotFoundException(String message) {
        super(message);
    }
    
}
