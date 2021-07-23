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

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.services.UserService;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserResource {

	private UserService userService;

	@Autowired
	public UserResource(final UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(
			@PageableDefault(page = 0, size = 10, sort = "firstName", direction = Direction.ASC) Pageable pageable) {
		Page<UserDTO> users = this.userService.findAllPaged(pageable);
		return ResponseEntity.ok().body(users);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
		UserDTO user = this.userService.findById(id);
		return ResponseEntity.ok().body(user);
	}

	@PostMapping
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {
		UserDTO newDTO = this.userService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(newDTO);
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<UserDTO> update(@Valid @RequestBody UserUpdateDTO dto, @PathVariable Long id) {
		UserDTO newDTO = this.userService.update(id, dto);
		return ResponseEntity.ok().body(newDTO);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		this.userService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
