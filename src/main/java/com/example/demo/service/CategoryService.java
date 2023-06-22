package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Category> getAllCategory(){
		return this.categoryRepository.findAll();
	}
	
	public Category findCategoryById(long id) {
		return this.categoryRepository.findById(id).orElse(null);
	}
	
	public Category addNewCategory(Map<String, Object> newCategory) {
		Category category = generateCategoryClass(newCategory);
		category = this.categoryRepository.save(category);
		return category;
	}
	
	public Category updateCategory(long id, Map<String,Object> newCategory) {
		Category category = generateCategoryClass(newCategory);
		category.setId(id);
		category = this.categoryRepository.save(category);
		return category;
	}
	
	public void deleteCategoryById(long id) {
		this.categoryRepository.deleteById(id);
	}
	
	private Category generateCategoryClass(Map<String, Object> newCategory) {
		Category category = new Category();
		category.setName(newCategory.get("name").toString());
		return category;
	}
}
