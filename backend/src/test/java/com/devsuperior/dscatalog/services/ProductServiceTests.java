package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.existingId = 1l;
		this.nonExistingId = 1000l;
		this.dependentId = 4l;
		product = Factory.createProduct();
		this.page = new PageImpl<>(List.of(product));

		Mockito.when(this.repository.find(ArgumentMatchers.anyList(), ArgumentMatchers.anyString(),
				ArgumentMatchers.any(Pageable.class))).thenReturn(page);
		Mockito.when(this.repository.save(ArgumentMatchers.any())).thenReturn(this.product);
		Mockito.when(this.repository.findById(this.existingId)).thenReturn(Optional.of(this.product));
		Mockito.when(this.repository.findById(this.nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(this.repository.getOne(this.existingId)).thenReturn(this.product);
		Mockito.when(this.repository.getOne(this.nonExistingId)).thenThrow(EntityNotFoundException.class);

		Mockito.doNothing().when(this.repository).deleteById(this.existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(this.repository).deleteById(this.nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(this.repository).deleteById(this.dependentId);
	}

	@Test
	public void insertShouldReturnProductDTO() {
		ProductDTO dto = new ProductDTO(this.product);
		dto = this.service.insert(dto);

		Assertions.assertNotNull(dto);
		Mockito.verify(this.repository, Mockito.times(1)).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO dto = this.service.findById(this.existingId);

		Assertions.assertNotNull(dto);
		Mockito.verify(this.repository, Mockito.times(1)).findById(this.existingId);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			this.service.findById(this.nonExistingId);
		});
		Mockito.verify(this.repository, Mockito.times(1)).findById(this.nonExistingId);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			this.service.delete(this.existingId);
		});
		Mockito.verify(this.repository, Mockito.times(1)).deleteById(this.existingId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			this.service.delete(this.nonExistingId);
		});
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdDependent() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			this.service.delete(this.dependentId);
		});
	}

}
