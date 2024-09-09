package com.fakestore.api.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fakestore.api.Exception.CategoryNotFoundException;
import com.fakestore.api.Exception.InvalidProductException;
import com.fakestore.api.model.Product;
import com.fakestore.api.model.Rating;


@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	private static final String FAKE_STORE_API = "https://fakestoreapi.com/products/";

	//To get the all products by category wise
	@Override
	public List<Product> getProductsByCategory(String category) {
		
	    String url = FAKE_STORE_API + "category/" + category;
		
	    try {
            ResponseEntity<Product[]> response = restTemplate.getForEntity(url, Product[].class);
            Product[] products = response.getBody();

            if (products == null || products.length == 0) {
                throw new CategoryNotFoundException("No products found for category: " + category);
            }

            return Arrays.asList(products);
        } catch (HttpClientErrorException.NotFound e) {
            throw new CategoryNotFoundException("Category not found: " + category);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch products from the category: " + category, e);
        }
		
	}
	
	//to add the product
    @Override
    public Product addProduct(Product product) {
		
	    // If the rating is not provided set a default value
        if (product.getRating() == null) {
            product.setRating(new Rating(0.0, 0));
        } else {
            validateRating(product.getRating());
        }
        
        validateProduct(product);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Product> request = new HttpEntity<>(product, headers);
        try {
            ResponseEntity<Product> response = restTemplate.postForEntity(FAKE_STORE_API, request, Product.class);
            return response.getBody();
        } catch (HttpClientErrorException.BadRequest e) {
            throw new InvalidProductException("Invalid product data provided.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to add product.", e);
        }
    }
	
    private void validateRating(Rating rating) {
        if (rating.getRate() < 0.0 || rating.getRate() > 5.0) {
            throw new InvalidProductException("Product rating must be between 0.0 and 5.0.");
        }

        if (rating.getCount() < 0) {
            throw new InvalidProductException("Rating count cannot be negative.");
        }
    }

    private void validateProduct(Product product) {
        if (product.getTitle() == null || product.getTitle().isEmpty()) {
            throw new InvalidProductException("Product title is required.");
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new InvalidProductException("Product price must be greater than zero.");
        }

        if (product.getRating() != null) {
            if (product.getRating().getRate() < 0.0 || product.getRating().getRate() > 5.0) {
                throw new InvalidProductException("Product rating must be between 0.0 and 5.0.");
            }
        }
    }

	
}
