package com.project.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.project.dscatalog.entities.Product;
import com.project.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	@Autowired
	private ProductRepository productRepository;
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}

	@Test
	public void deleteShoultDeleteObjectWhenIdExists() {
		productRepository.deleteById(existingId);
		Optional<Product> result = productRepository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAcessExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			productRepository.deleteById(nonExistingId);
		});
	}
	
	@Test
	public void saveShoultPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createdProduct();
		product.setId(null);
		
		product = productRepository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void findByIdShoulRetunOptionalProductWhenIdExists () {
		Optional<Product> product = productRepository.findById(existingId);
		Assertions.assertTrue(product.isPresent());
	}
	
	@Test
	public void findByIdShoulRetunOptionalProductWhenIdDoesNotExists () {
		Optional<Product> product = productRepository.findById(nonExistingId);
		Assertions.assertFalse(product.isPresent());
	}
}
