package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private long existingId;
	private long nonExistingId;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.existingId = 1l;
		this.nonExistingId = 1000l;
		
		Mockito.doNothing().when(this.repository).deleteById(this.existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(this.repository).deleteById(this.nonExistingId);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			this.service.delete(this.existingId);
		});
		Mockito.verify(this.repository, Mockito.times(1)).deleteById(this.existingId);
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			this.service.delete(this.nonExistingId);
		});
		
	}

}
