package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class ProductServiceIT {

	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long totalProductsAmount;
	
	@BeforeEach
	public void setup() {
		this.existingId = 1l;
		this.nonExistingId = 1000l;
		this.totalProductsAmount = 25l;
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		this.service.delete(this.existingId);
		Assertions.assertEquals(this.totalProductsAmount - 1, this.repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			this.service.delete(this.nonExistingId);
		});
	}
	
}
