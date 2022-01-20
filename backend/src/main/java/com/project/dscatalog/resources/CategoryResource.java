package com.project.dscatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dscatalog.entities.CategoryEntity;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	@Autowired
	
	@GetMapping
	public ResponseEntity<List<CategoryEntity>> findAll(){
		List<CategoryEntity> list = new ArrayList<>();
		list.add(new CategoryEntity(1L, "Books"));
		list.add(new CategoryEntity(1L, "Books"));
		return ResponseEntity.ok().body(list);
	}
}
 