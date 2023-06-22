package com.example.demo.DTO;

import java.util.Map;

import lombok.Data;

@Data
public class CategoryDTORequest {
	private String name;
	public CategoryDTORequest(Map<String,Object> category) {
		this.name = category.get("name").toString();
	}
}
