package com.project.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.project.dscatalog.entities.CategoryEntity;
import com.project.dscatalog.entities.ProductEntity;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String description;
	private Double price;
	private String imgUrl;
	private Instant date;

	private List<CategoryDTO> categoryDTOs = new ArrayList<>();

	public ProductDTO() {
	}

	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}

	public ProductDTO(ProductEntity productEntity) {
		this.id = productEntity.getId();
		this.name = productEntity.getName();
		this.description = productEntity.getDescription();
		this.price = productEntity.getPrice();
		this.imgUrl = productEntity.getImgUrl();
		this.date = productEntity.getDate();
	}
	
	
	public ProductDTO(ProductEntity productEntity, Set<CategoryEntity> categoryEntities) {
		this(productEntity);
		categoryEntities.forEach(cat -> this.categoryDTOs.add(new CategoryDTO(cat))); //this.categoryDTOs.add(new CategoryDTO(cat))
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategoryDTOs() {
		return categoryDTOs;
	}

	public void setCategoryDTOs(List<CategoryDTO> categoryDTOs) {
		this.categoryDTOs = categoryDTOs;
	}

}
