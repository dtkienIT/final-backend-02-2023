package com.example.demo.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.ProductDTOResponse;
import com.example.demo.constant.ResponseCode;
import com.example.demo.model.Product;
import com.example.demo.model.VariantProduct;
import com.example.demo.service.VariantProductService;


@RestController
@RequestMapping("/variantProducts")
public class VariantProductController extends BaseRestController{
	@Autowired
	private VariantProductService variantProductService;
	
	@GetMapping("/product/{productId}")
    public ResponseEntity<?> getAllVariantProductsByProductId(@PathVariable Long productId) {
       try {
    	   List<VariantProduct> variantProducts = variantProductService.getAllVariantProductsByProductId(productId);
    	   return super.success(variantProducts);
	} catch (Exception e) {
		e.printStackTrace();
	}
       return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
	
	@GetMapping("")
    public ResponseEntity<?> getAllVariantProducts() {
       try {
    	   List<VariantProduct> variantProducts = variantProductService.getAllVariantProducts();
    	   return super.success(variantProducts);
	} catch (Exception e) {
		e.printStackTrace();
	}
       return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
	
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping("/{id}")
	@Transactional(noRollbackFor = { Exception.class })
	public ResponseEntity<?> updateVariantProduct(@PathVariable Long id,@RequestBody Map<String,Object> newVariantProduct){
		try {
			if (ObjectUtils.isEmpty(newVariantProduct) || ObjectUtils.isEmpty(newVariantProduct.get("name"))
			|| ObjectUtils.isEmpty(newVariantProduct.get("model_year"))|| ObjectUtils.isEmpty(newVariantProduct.get("price"))
			|| ObjectUtils.isEmpty(newVariantProduct.get("color"))|| ObjectUtils.isEmpty(newVariantProduct.get("productId"))) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}
			
			VariantProduct foundVariantProduct = this.variantProductService.getVariantProductsById(id);
			if (ObjectUtils.isEmpty(foundVariantProduct)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}

			this.variantProductService.updateVariantProduct(id, newVariantProduct);
			return super.success(newVariantProduct);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping("")
	@Transactional(noRollbackFor = { Exception.class })
	public ResponseEntity<?> createVariantProduct(@RequestBody Map<String,Object> newVariantProduct){
		try {
			if (ObjectUtils.isEmpty(newVariantProduct) || ObjectUtils.isEmpty(newVariantProduct.get("name"))
			|| ObjectUtils.isEmpty(newVariantProduct.get("model_year"))|| ObjectUtils.isEmpty(newVariantProduct.get("price"))
			|| ObjectUtils.isEmpty(newVariantProduct.get("color"))|| ObjectUtils.isEmpty(newVariantProduct.get("productId"))) {
			return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
		}
			
			this.variantProductService.addVariantProduct(newVariantProduct);
			return super.success(newVariantProduct);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
}
