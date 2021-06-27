package com.devsuperior.dscatalog.resources;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

	@Autowired
	private MockMvc mockMvc;

	private Long existingId;
	private Long nonExistingId;
	private Long totalProducts;

	@BeforeEach
	public void setup() {
		this.existingId = 1l;
		this.nonExistingId = 1000l;
		this.totalProducts = 25l;
	}

	@Test
	public void findAllShouldReturnSortedPageWhenSortByPrice() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/products?page=0&size=10&sort=price,asc")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(this.totalProducts))
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(new BigDecimal("90.5")));
	}
}
