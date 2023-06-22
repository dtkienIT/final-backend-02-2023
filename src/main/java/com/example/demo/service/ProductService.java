package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
    private ProductRepository productRepository;
    
	@Autowired
    private CategoryRepository categoryRepository;
	
    public List<Product> getAllProductsByCategory(Long categoryId) {
        return this.productRepository.findByCategoryId(categoryId);
    }
	
    public Product updateProduct(long id, Map<String, Object> newProduct) {
		Product product = new Product();
		product.setName(newProduct.get("name").toString());

		Long categoryId = Long.parseLong(newProduct.get("categoryId").toString());
		Category foundcategory = this.categoryRepository.findById(categoryId).orElse(null);
		product.setCategory(foundcategory);
		product.setId(id);
		product = this.productRepository.save(product);
		return product;
	}
    
    public Product addProduct(Map<String, Object> newProduct) {
		Product product = new Product();
		product.setName(newProduct.get("name").toString());

		Long categoryId = Long.parseLong(newProduct.get("categoryId").toString());
		Category foundcategory = this.categoryRepository.findById(categoryId).orElse(null);
		product.setCategory(foundcategory);
		product = this.productRepository.save(product);
		return product;
	}
    
    public Product getProductsById(Long id) {
        return this.productRepository.findById(id).orElse(null);
    }
}
