package com.devsuperior.dscatalog.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

@RestController
@RequestMapping(path = "/api/v1/categories")
public class CategoryResource {

	private CategoryService categoryService;

	@Autowired
	public CategoryResource(final CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAll(@PageableDefault(page = 0, size = 5, sort = "name", direction = Direction.ASC) Pageable pageable) {
		Page<CategoryDTO> categories = this.categoryService.findAllPaged(pageable);
		return ResponseEntity.ok().body(categories);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		CategoryDTO category = this.categoryService.findById(id);
		return ResponseEntity.ok().body(category);
	}

	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@Valid @RequestBody CategoryDTO dto) {
		dto = this.categoryService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<CategoryDTO> update(@Valid @RequestBody CategoryDTO dto, @PathVariable Long id) {
		dto = this.categoryService.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		this.categoryService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
