package com.project.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dscatalog.dto.ProductDTO;
import com.project.dscatalog.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	private Long existingId;
	private Long nonExistingId;
	private Long countTotalproducts;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalproducts = 25L;
	}
	
	@Test
	public void updateShoultRetrunProductDTOWhenIdExists() throws Exception {
		ProductDTO productDTO = Factory.createdProductDTO();
		String expectedName = productDTO.getName();
		String expetedDescription = productDTO.getDescription();
		String jsonbody = objectMapper.writeValueAsString(productDTO);
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expetedDescription));
	};
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
		ResultActions result = mockMvc.perform(get("/products?page0&size=10&sort=name,asc")
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.totalElements").value(countTotalproducts));
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
	}
	
	@Test
	public void updateShoultReturnNotFoundProductDTOWhenIdDoesNotExists() throws Exception {
		ProductDTO productDTO = Factory.createdProductDTO();
		String jsonbody = objectMapper.writeValueAsString(productDTO);
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());

	};
}
