package com.project.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dscatalog.dto.CategoryDTO;
import com.project.dscatalog.entities.CategoryEntity;
import com.project.dscatalog.repositories.CategoryRepository;
import com.project.dscatalog.services.exceptions.DataBaseException;
import com.project.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAll(Pageable pageable) {
		Page<CategoryEntity> list = categoryRepository.findAll(pageable);
		return list.map(CategoryDTO::new); // (v -> new CategoryDTO(v))
	}
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
		Page<CategoryEntity> list = categoryRepository.findAll(pageRequest);
		return list.map(CategoryDTO::new); // (v -> new CategoryDTO(v))
	}

	public CategoryDTO findById(Long id) {
		Optional<CategoryEntity> optional = Optional.ofNullable(
				categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
		return new CategoryDTO(optional.get());
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO categoryDTO) {
		return new CategoryDTO(CategoryDTOtoCategoryEntity(categoryDTO));
	}

	private CategoryEntity CategoryDTOtoCategoryEntity(CategoryDTO categoryDTO) {
		CategoryEntity categoryEntity = new CategoryEntity();
		categoryEntity.setName(categoryDTO.getName());
		return categoryRepository.save(categoryEntity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
		try {
			CategoryEntity categoryEntity = categoryRepository.getOne(id);
			categoryEntity.setName(categoryDTO.getName());
			return new CategoryDTO(categoryRepository.save(categoryEntity));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id: " + id + " not found");
		}
	}

	public void delete(Long id) {
		try {
			categoryRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("ID: " + id + " not found.");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
	}

}
