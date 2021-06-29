package com.devsuperior.dscatalog.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

	private BCryptPasswordEncoder passwordEncoder;
	
	private UserRepository userRepository;

	private RoleRepository roleRepository;
	
	@Autowired
	public UserService(final UserRepository userRepository, final RoleRepository roleRepository, final BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> users = this.userRepository.findAll(pageable);
		return users.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return new UserDTO(user);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		this.setUserData(dto, entity);
		entity.setPassword(this.passwordEncoder.encode(dto.getPassword()));
		entity = this.userRepository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserInsertDTO dto) {
		try {
			User entity = this.userRepository.getOne(id);
			this.setUserData(dto, entity);
			entity.setPassword(this.passwordEncoder.encode(dto.getPassword()));
			entity = this.userRepository.save(entity);
			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found - " + id);
		}
	}

	public void delete(Long id) {
		try {
			this.userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException | IllegalArgumentException e) {
			throw new ResourceNotFoundException("Id not found - " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void setUserData(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getLastName());
		
		entity.getRoles().clear();
		dto.getRoles().forEach(roleDTO -> {
			Role role = this.roleRepository.getOne(roleDTO.getId());
			entity.getRoles().add(role);
		});
		
	}

}
