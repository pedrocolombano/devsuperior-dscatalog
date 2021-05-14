package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.services.CategoryService;

@RestController
@RequestMapping(path = "/api/v1/categories")
public class CategoryResource {

	private CategoryService categoryService;
	
	public CategoryResource(final CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDto>> findAll() {
		List<CategoryDto> categories = this.categoryService.findAll();
		return ResponseEntity.ok().body(categories);
	}
	
}
