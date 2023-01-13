package com.project.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.project.dscatalog.dto.ProductDTO;
import com.project.dscatalog.entities.Category;
import com.project.dscatalog.entities.Product;
import com.project.dscatalog.repositories.CategoryRepository;
import com.project.dscatalog.repositories.ProductRepository;
import com.project.dscatalog.services.exceptions.DataBaseException;
import com.project.dscatalog.services.exceptions.ResourceNotFoundException;
import com.project.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryRepository categoryRepository;

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO productDTO;
	private Category category;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		category = Factory.createdCategory();
		product = Factory.createdProduct();
		productDTO = Factory.createdProductDTO();
		page = new PageImpl<>(List.of(product));

		Mockito.when(productRepository.getOne(existingId)).thenReturn(product);
		Mockito.when(productRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty()); // Optional vazio

		Mockito.doNothing().when(productRepository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
	}

	@Test
	public void updateShoultThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.update(nonExistingId, productDTO);
		});
	}

	@Test
	public void findByIdShoultThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.findById(nonExistingId);
		});
	}

	@Test
	public void findByIdShoultReturnProductDTOWhenIdExists() {
		ProductDTO productDTO = productService.findById(existingId);
		Assertions.assertNotNull(productDTO);
		Mockito.verify(productRepository).findById(existingId);
	}

	@Test
	public void findAllShoultReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = productService.findAll(pageable);
		Assertions.assertNotNull(result);
		Mockito.verify(productRepository).findAll(pageable);
	}

	@Test
	public void deleteShoultThrowDataBaseExceptionWhenDependentId() {
		Assertions.assertThrows(DataBaseException.class, () -> {
			productService.delete(dependentId);
		});
	}

	@Test
	public void deleteShoultThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.delete(nonExistingId);
		});
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShoultDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			productService.delete(existingId);
		});
		Mockito.verify(productRepository).deleteById(existingId);
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
	}
}
