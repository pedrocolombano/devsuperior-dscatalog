package com.devsuperior.dscatalog.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryService(final CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Transactional(readOnly = true)
	public List<CategoryDto> findAll() {
		List<Category> categories = this.categoryRepository.findAll();
		return Collections
				.unmodifiableList(categories.stream().map(x -> new CategoryDto(x)).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	public CategoryDto findById(Long id) {
		Category category = this.categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		return new CategoryDto(category);
	}

	@Transactional
	public CategoryDto insert(CategoryDto dto) {
		Category category = new Category(null, dto.getName());
		category = this.categoryRepository.save(category);
		return new CategoryDto(category);
	}

	@Transactional
	public CategoryDto update(Long id, CategoryDto dto) {
		try {
			Category entity = this.categoryRepository.getOne(id);
			entity.setName(dto.getName());
			entity = this.categoryRepository.save(entity);
			return new CategoryDto(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found - " + id);
		}
	}

	public void delete(Long id) {
		try {
			this.categoryRepository.deleteById(id);
		} catch (EmptyResultDataAccessException | IllegalArgumentException e) {
			throw new ResourceNotFoundException("Id not found - " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

}
