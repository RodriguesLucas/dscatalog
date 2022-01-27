package com.project.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
