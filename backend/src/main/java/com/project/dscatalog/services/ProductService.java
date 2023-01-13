package com.project.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dscatalog.dto.CategoryDTO;
import com.project.dscatalog.dto.ProductDTO;
import com.project.dscatalog.entities.Category;
import com.project.dscatalog.entities.Product;
import com.project.dscatalog.repositories.CategoryRepository;
import com.project.dscatalog.repositories.ProductRepository;
import com.project.dscatalog.services.exceptions.DataBaseException;
import com.project.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> list = productRepository.findAll(pageable);
        return list.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> optional = productRepository.findById(id);
        Product productEntity = optional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(productEntity, productEntity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product productEntity = new Product();
        copyDtoToEntity(productDTO, productEntity);
        productEntity = productRepository.save(productEntity);
        return new ProductDTO(productEntity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product productEntity = productRepository.getOne(id);
            copyDtoToEntity(productDTO, productEntity);
            return new ProductDTO(productRepository.save(productEntity));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id: " + id + " not found");
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("ID: " + id + " not found.");
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDTO productDTO, Product productEntity) {
        productEntity.setName(productDTO.getName());
        productEntity.setDate(productDTO.getDate());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setImgUrl(productDTO.getImgUrl());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.getCategories().clear();
        for (CategoryDTO dto : productDTO.getCategories()) {
            Category categoryEntity = categoryRepository.getOne(dto.getId());
            productEntity.getCategories().add(categoryEntity);
        }
    }

}
