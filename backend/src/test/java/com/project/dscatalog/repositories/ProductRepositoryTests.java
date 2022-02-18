package com.project.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.project.dscatalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTests {
	@Autowired
	private ProductRepository productRepository;

	@Test
	public void deleteShoultDeleteObjectWhenIdExists() {
		// Arrange
		Long validId = 1L;
		
		// Act
		productRepository.deleteById(1L);
		
		// Assert
		Optional<Product> result = productRepository.findById(validId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAcessExceptionWhenIdDoesNotExist() {
		Long nonExistId = 20L;
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			productRepository.deleteById(nonExistId);
		});
	}
}
