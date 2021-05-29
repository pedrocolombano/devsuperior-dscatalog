package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService service;

	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;
	private long existingId;
	private long nonExistingId;

	@BeforeEach
	public void setup() {
		this.productDTO = Factory.createProductDTO();
		this.page = new PageImpl<>(List.of(productDTO));
		this.existingId = 1l;
		this.nonExistingId = 2l;

		Mockito.when(this.service.findAllPaged(ArgumentMatchers.any())).thenReturn(this.page);
		Mockito.when(this.service.findById(this.existingId)).thenReturn(productDTO);
		Mockito.when(this.service.findById(this.nonExistingId)).thenThrow(ResourceNotFoundException.class);
	}

	@Test
	public void findAllShouldReturnProductDTOPage() throws Exception {
		ResultActions result = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/products").accept(MediaType.APPLICATION_JSON));
		result.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", this.existingId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", this.nonExistingId)
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

}
