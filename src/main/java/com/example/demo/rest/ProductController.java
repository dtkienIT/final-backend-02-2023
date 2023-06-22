package com.example.demo.rest;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.CategoryDTOResponse;
import com.example.demo.DTO.ProductDTOResponse;
import com.example.demo.constant.ResponseCode;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController extends BaseRestController{
	@Autowired
	private ProductService productService;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getAllProductsByCategory(@PathVariable Long categoryId, 
    		@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "20") Integer limit) {
       try {
    	   List<Product> products = productService.getAllProductsByCategory(categoryId);
    	   
    	   Pageable pageable = PageRequest.of(offset, limit);
			List<Order> orders = new ArrayList<>();
			if (!orders.isEmpty()) {
				pageable = PageRequest.of(offset, limit, Sort.by(orders));
			}
			
			int start = (int) pageable.getOffset();
			int end = Math.min((start + pageable.getPageSize()), products.size());
			List<Product> pagedProducts = products.subList(start, end);
			List<ProductDTOResponse> response = pagedProducts.stream()
			    .map(ProductDTOResponse::new)
			    .collect(Collectors.toList());
    	   return super.success(response);
	} catch (Exception e) {
		e.printStackTrace();
	}
       return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/{id}")
    public ResponseEntity<?> getProductsById(@PathVariable Long id) {
       try {
    	   Product foundProduct = this.productService.getProductsById(id);
			if(ObjectUtils.isEmpty(foundProduct)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}
			return super.success(foundProduct);	
	} catch (Exception e) {
		e.printStackTrace();
	}
       return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
	
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping("/{id}")
	@Transactional(noRollbackFor = { Exception.class })
	public ResponseEntity<?> updateProduct(@PathVariable Long id,@RequestBody Map<String,Object> newProduct){
		try {
			if (ObjectUtils.isEmpty(newProduct) || ObjectUtils.isEmpty(newProduct.get("name"))
					|| ObjectUtils.isEmpty(newProduct.get("categoryId"))) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}
			
			Product foundProduct = this.productService.getProductsById(id);
			if (ObjectUtils.isEmpty(foundProduct)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}

			this.productService.updateProduct(id, newProduct);
			return super.success(newProduct);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping("")
	@Transactional(noRollbackFor = { Exception.class })
	public ResponseEntity<?> createProduct(@RequestBody Map<String,Object> newProduct){
		try {
			if (ObjectUtils.isEmpty(newProduct) || ObjectUtils.isEmpty(newProduct.get("name"))
					|| ObjectUtils.isEmpty(newProduct.get("categoryId"))) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}
			
			this.productService.addProduct(newProduct);
			return super.success(newProduct);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
}
