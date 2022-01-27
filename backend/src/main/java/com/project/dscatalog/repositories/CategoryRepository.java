package com.project.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {


}
