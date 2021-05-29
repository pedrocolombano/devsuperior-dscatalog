package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long totalProductsAmount;
	
	@BeforeEach
	public void setup() {
		this.existingId = 1l;
		this.nonExistingId = 1000l;
		this.totalProductsAmount = 25l;
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = this.repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(this.totalProductsAmount + 1, product.getId());
	}
	
	@Test
	public void findByIdShouldReturnNotNullOptionalWhenIdExists(){
		Optional<Product> product = this.repository.findById(this.existingId);
		Assertions.assertTrue(product.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnNullOptionalWhenIdDoesNotExists() {
		Optional<Product> product = this.repository.findById(this.nonExistingId);
		Assertions.assertTrue(product.isEmpty());
	}
	
	@Test 
	public void deleteShouldDeleteObjectWhenIdExists() {
		this.repository.deleteById(existingId);
		Optional<Product> result = this.repository.findById(existingId);
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			this.repository.deleteById(nonExistingId);
		});
	}
	
}
