package com.project.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.dscatalog.entities.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

}
