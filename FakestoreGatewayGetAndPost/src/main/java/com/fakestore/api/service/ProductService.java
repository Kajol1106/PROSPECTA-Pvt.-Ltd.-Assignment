package com.fakestore.api.service;

import java.util.List;

import com.fakestore.api.model.Product;

public interface ProductService {

	public List<Product> getProductsByCategory(String category);
	
	public Product addProduct(Product product);
}
