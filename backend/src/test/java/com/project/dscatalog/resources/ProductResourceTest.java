package com.project.dscatalog.resources;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dscatalog.dto.ProductDTO;
import com.project.dscatalog.services.ProductService;
import com.project.dscatalog.services.exceptions.DataBaseException;
import com.project.dscatalog.services.exceptions.ResourceNotFoundException;
import com.project.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductService productService;
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		productDTO = Factory.createdProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		when(productService.findAll(ArgumentMatchers.any())).thenReturn(page);

		when(productService.findById(existingId)).thenReturn(productDTO);
		when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		when(productService.update(eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);
		when(productService.update(eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
		
		when(productService.insert(ArgumentMatchers.any())).thenReturn(productDTO);
		
		doNothing().when(productService).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
		doThrow(DataBaseException.class).when(productService).delete(dependentId);
	}
	
	@Test
	public void deleteShoultReturnNoContentWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShoultReturnNotFoundtWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void insertShoultReturnProductDTOCreated () throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		ResultActions result = mockMvc.perform(post("/products")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void updateShoultRetrunProductDTOWhenIdExists() throws Exception {
		String jsonbody = objectMapper.writeValueAsString(productDTO);
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	};

	@Test
	public void updateShoultRetrunNotFoundWhenIdDoesNotExists() throws Exception {
		String jsonbody = objectMapper.writeValueAsString(productDTO);
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	};

	@Test
	public void findAllShoultReturnPage() throws Exception {
		ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}

	@Test
	public void findByIdShoultReturnProductWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void findByIdShoultReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
}
