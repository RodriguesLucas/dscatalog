package com.project.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.project.dscatalog.dto.ProductDTO;
import com.project.dscatalog.repositories.ProductRepository;
import com.project.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional //Roda o test e da rollback no banco
public class ProductServiceIT {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalproducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalproducts = 25L;
	}
	
	@Test
	public void findAllShoultReturSortedPageWhenSortByName () {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		Page<ProductDTO> result = productService.findAll(pageRequest);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}
	
	@Test
	public void findAllShoultReturnEmptyPageWhenPageDoesNotExist () {
		PageRequest pageRequest = PageRequest.of(50, 10);
		Page<ProductDTO> result = productService.findAll(pageRequest);
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllShoultReturnPageWhenPage0Size10 () {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<ProductDTO> result = productService.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalproducts, result.getTotalElements());
	}
	
	@Test
	public void deleteShoultThrowResourceNotFoundExceptionWhenIdDoesNotExist () {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.delete(nonExistingId);
		});
	}
	
	@Test
	public void deleteShoultDeleteResourceWhenIdExists() {
		productService.delete(existingId);
		Assertions.assertEquals(countTotalproducts - 1, productRepository.count());
	}
	
}
