package com.devsuperior.dscatalog.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

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
		return Collections.unmodifiableList(categories.stream().map(x -> new CategoryDto(x)).collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	public CategoryDto findById(Long id) {
		Category category = this.categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
		return new CategoryDto(category);
	}
	
}
