package com.project.dscatalog.resources;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import com.project.dscatalog.dto.ProductDTO;
import com.project.dscatalog.services.ProductService;
import com.project.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductService productService;
	
	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;
	
	@BeforeEach
	void setUp() throws Exception {
		productDTO = Factory.createdProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		when(productService.findAll(ArgumentMatchers.any())).thenReturn(page);
	}
	
	@Test
	public void findAllShoultReturnPage() throws Exception {
		mockMvc.perform(get("/products")).andExpect(status().isOk());
	}
	
}
