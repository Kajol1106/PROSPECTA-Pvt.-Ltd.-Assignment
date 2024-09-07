# FakestoreGatewayGetAndPostApplication

**FakestoreGatewayGetAndPostApplication** is a Spring Boot application that interacts with the [FakeStore API](https://fakestoreapi.com/) to manage products in an e-commerce platform. This API allows listing products by category, adding new products, and handles various edge cases and exceptions.

## Table of Contents
1. [Project Overview](#project-overview)
2. [Setup Instructions](#setup-instructions)
3. [API Endpoints](#api-endpoints)
   - [List Products by Category](#list-products-by-category)
   - [Add a New Product](#add-a-new-product)
4. [Exception Handling](#exception-handling)
5. [Testing](#testing)

## Project Overview

The FakestoreGatewayGetAndPostApplication provides two main functionalities:
1. **Listing products by category**: Retrieves products based on the provided category from the FakeStore API.
2. **Adding a new product**: Allows adding new products with optional rating data.

## Setup Instructions

To set up the project locally, follow these steps:
1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/PROSPECTA-Pvt.-Ltd.-Assignment.git
2. **Navigate to the Project Directory**
   ```bash
   cd PROSPECTA-Pvt.-Ltd.-Assignment
3. **Build and Run the Application**
   ```bash
   mvn clean install
   mvn spring-boot:run
2. **Access the Application**
   ```bash
   The application will be available at http://localhost:8080.

## API Endpoints

1. **List Products by Category**
   1. Endpoint: GET /api/products/category/{category}
   2. Description: Retrieves a list of products based on the category provided.
   3. Parameters:
      1. category (Path Variable): The category of products to retrieve (e.g., electronics, jewelery).
   4. Example Request and Response:
   ```bash
   Request : 
   http://localhost:8080/api/products/category/electronics

   Response :
   [
    {
        "id": 5,
        "title": "John Hardy Women's Legends Naga Gold & Silver Dragon Station Chain Bracelet",
        "price": 695,
        "description": "From our Legends Collection, the Naga was inspired by the mythical water dragon...",
        "category": "jewelery",
        "image": "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_.jpg",
        "rating": {
            "rate": 4.6,
            "count": 400
        }
    }
   ]

3. **Response Codes:**
   ```bash
   200 OK: Successfully retrieved the list of products.
   404 Not Found: No products found for the given category.

2. **Add a New Product**
   1. Endpoint: POST /api/products/add
   2. Description: Adds a new product with the provided data. If no rating is provided, a default rating will be set.
   
   3. Example Request Body and Response:
   ```bash
   Request Body :
   {
    "title": "New Product",
    "description": "Description of the new product",
    "price": 299.99,
    "category": "electronics",
    "image": "image_url",
    "rating": {
        "rate": 4.8,
        "count": 250
    }
   }

   Response :
    {
    "id": 21,
    "title": "New Product",
    "description": "Description of the new product",
    "price": 299.99,
    "category": "electronics",
    "image": "image_url",
    "rating": {
        "rate": 4.8,
        "count": 250
    }
   }

3. **Response Codes:**
   ```bash
   201 Created: Successfully added the new product.
   400 Bad Request: Invalid product data or missing required fields.

## Exception Handling
**The API includes comprehensive exception handling for:**

1. ProductNotFoundException: Thrown when a requested product is not found.
2. CategoryNotFoundException: Thrown when no products are found for a given category.
3. InvalidProductException: Thrown when the provided product data is invalid.
4. Generic Exception: Handles any other unexpected errors.

## Testing
**Test the API using tools like Postman**
   

