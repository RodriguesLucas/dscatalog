package com.project.dscatalog.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dscatalog.entities.CategoryEntity;
import com.project.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryEntity> findAll() {
		return categoryRepository.findAll();
	}
}
