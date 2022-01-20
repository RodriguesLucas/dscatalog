package com.project.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.dscatalog.entities.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
