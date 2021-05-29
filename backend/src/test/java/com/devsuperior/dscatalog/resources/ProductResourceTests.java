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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService service;

	@Autowired
	private ObjectMapper objectMapper;

	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;
	private long existingId;
	private long nonExistingId;
	private long dependentId;

	@BeforeEach
	public void setup() {
		this.productDTO = Factory.createProductDTO();
		this.page = new PageImpl<>(List.of(productDTO));
		this.existingId = 1l;
		this.nonExistingId = 2l;
		this.dependentId = 3l;

		Mockito.when(this.service.findAllPaged(ArgumentMatchers.any())).thenReturn(this.page);
		Mockito.when(this.service.findById(this.existingId)).thenReturn(productDTO);
		Mockito.when(this.service.findById(this.nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(this.service.insert(ArgumentMatchers.any())).thenReturn(this.productDTO);
		Mockito.when(this.service.update(ArgumentMatchers.eq(this.existingId), ArgumentMatchers.any()))
				.thenReturn(productDTO);
		Mockito.when(this.service.update(ArgumentMatchers.eq(this.nonExistingId), ArgumentMatchers.any()))
				.thenThrow(ResourceNotFoundException.class);
		Mockito.doNothing().when(this.service).delete(this.existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(this.service).delete(this.nonExistingId);
		Mockito.doThrow(DatabaseException.class).when(this.service).delete(this.dependentId);
	}

	@Test
	public void findAllShouldReturnProductDTOPage() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
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

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		String jsonBody = this.objectMapper.writeValueAsString(this.productDTO);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", this.existingId).content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
	}

	@Test
	public void insertShouldReturnCreatedAndProductDTO() throws Exception {
		String jsonBody = this.objectMapper.writeValueAsString(this.productDTO);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/api/v1/products").content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		String jsonBody = this.objectMapper.writeValueAsString(this.productDTO);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", this.nonExistingId).content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", this.existingId)
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", this.nonExistingId)
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void deleteShouldReturnBadRequestWhenIdIsDependent() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", this.dependentId)
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

}
