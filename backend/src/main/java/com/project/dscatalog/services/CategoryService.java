package com.project.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dscatalog.entities.CategoryEntity;
import com.project.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	public List<CategoryEntity> findAll() {
		return categoryRepository.findAll();
	}
}
