package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.VariantProduct;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.VariantProductRepository;

@Service
public class VariantProductService {
	@Autowired
	private VariantProductRepository variantProductRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public List<VariantProduct> getAllVariantProductsByProductId(Long productId) {
        return this.variantProductRepository.findVariantProductByProductId(productId);
    }
	
	public List<VariantProduct> getAllVariantProducts() {
        return this.variantProductRepository.findAll();
    }
	
	public VariantProduct updateVariantProduct(long id, Map<String, Object> newVariantProduct) {
		VariantProduct variantProduct = new VariantProduct();
		variantProduct.setName(newVariantProduct.get("name").toString());
		variantProduct.setModel_year(newVariantProduct.get("model_year").toString());
		variantProduct.setPrice(Double.parseDouble(newVariantProduct.get("price").toString()));
		variantProduct.setColor(newVariantProduct.get("color").toString());

		Long productId = Long.parseLong(newVariantProduct.get("productId").toString());
		Product foundProduct = this.productRepository.findById(productId).orElse(null);
		variantProduct.setProduct(foundProduct);
		variantProduct.setId(id);
		variantProduct = this.variantProductRepository.save(variantProduct);
		return variantProduct;
	}
    
    public VariantProduct addVariantProduct(Map<String, Object> newVariantProduct) {
    	VariantProduct variantProduct = new VariantProduct();
		variantProduct.setName(newVariantProduct.get("name").toString());
		variantProduct.setModel_year(newVariantProduct.get("model_year").toString());
		variantProduct.setPrice(Double.parseDouble(newVariantProduct.get("price").toString()));
		variantProduct.setColor(newVariantProduct.get("color").toString());

		Long productId = Long.parseLong(newVariantProduct.get("productId").toString());
		Product foundProduct = this.productRepository.findById(productId).orElse(null);
		variantProduct.setProduct(foundProduct);
		variantProduct = this.variantProductRepository.save(variantProduct);
		return variantProduct;
	}
    
	public VariantProduct getVariantProductsById(Long id) {
        return this.variantProductRepository.findById(id).orElse(null);
    }
}
