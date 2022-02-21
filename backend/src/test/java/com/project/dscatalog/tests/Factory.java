package com.project.dscatalog.tests;

import java.time.Instant;

import com.project.dscatalog.dto.ProductDTO;
import com.project.dscatalog.entities.Category;
import com.project.dscatalog.entities.Product;

public class Factory {
	public static Product createdProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.now());
		product.getCategories().add(new Category(2L, "Electronics"));
		return product;
	}

	public static ProductDTO createdProductDTO() {
		Product product = createdProduct();
		return new ProductDTO(product, product.getCategories());
	}
}
