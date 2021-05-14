package com.devsuperior.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

	@GetMapping(path = "/{id}")
	public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
		CategoryDto category = this.categoryService.findById(id);
		return ResponseEntity.ok().body(category);
	}

	@PostMapping
	public ResponseEntity<CategoryDto> insert(@RequestBody CategoryDto dto) {
		dto = this.categoryService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<CategoryDto> update(@RequestBody CategoryDto dto, @PathVariable Long id) {
		dto = this.categoryService.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}

}
