package com.project.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dscatalog.dto.CategoryDTO;
import com.project.dscatalog.entities.CategoryEntity;
import com.project.dscatalog.repositories.CategoryRepository;
import com.project.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<CategoryEntity> list = categoryRepository.findAll();
		return list.stream().map(CategoryDTO::new).collect(Collectors.toList()); // (v -> new CategoryDTO(v))
	}

	public CategoryDTO findById(Long id) {
		Optional<CategoryEntity> optional = Optional.ofNullable(
				categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found")));
		return new CategoryDTO(optional.get());
	}
	
	
}
