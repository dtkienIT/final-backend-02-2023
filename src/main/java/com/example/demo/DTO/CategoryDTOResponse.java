package com.example.demo.DTO;

import java.util.List;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import com.example.demo.model.Category;
import com.example.demo.model.Product;

import lombok.Data;

@Data
public class CategoryDTOResponse {
	private Long id;
	private String name;
	public CategoryDTOResponse(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}
}
