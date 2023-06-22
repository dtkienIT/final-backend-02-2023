package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Product;
import com.example.demo.model.VariantProduct;

@Repository
public interface VariantProductRepository extends JpaRepository<VariantProduct, Long>{
	List<VariantProduct> findVariantProductByProductId(Long productId);
}
