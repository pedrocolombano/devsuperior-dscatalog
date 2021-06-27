package com.devsuperior.dscatalog.services;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
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
	public void findAllPagedShouldReturnPageWhenPageZeroAndSizeTen() {
		PageRequest page = PageRequest.of(0, 10);
		Page<ProductDTO> result = this.service.findAllPaged(page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(this.totalProductsAmount, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageFiftyAndSizeTen() {
		PageRequest page = PageRequest.of(50, 10);
		Page<ProductDTO> result = this.service.findAllPaged(page);
		Assertions.assertTrue(result.isEmpty());
		Assertions.assertEquals(50, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(this.totalProductsAmount, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnSortPageWhenSortByPrice() {
		PageRequest page = PageRequest.of(0, 10, Direction.DESC, "price");
		Page<ProductDTO> result = this.service.findAllPaged(page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(new BigDecimal("4170.00"), result.getContent().get(0).getPrice());
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
