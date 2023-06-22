package com.example.demo.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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

import com.example.demo.DTO.CategoryDTORequest;
import com.example.demo.DTO.CategoryDTOResponse;
import com.example.demo.constant.ResponseCode;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;



import com.example.demo.rest.BaseRestController;


@RestController
@RequestMapping(path = "/categories")
public class CategoryController extends BaseRestController{	
	@Autowired
	private CategoryService categoryService;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("")
	public ResponseEntity<?> getAllCategory(){
		try {
			List<Category> category = this.categoryService.getAllCategory();
			List<CategoryDTOResponse> response = category.stream().map(CategoryDTOResponse::new)
					.toList();
			return super.success(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/getById")
	public ResponseEntity<?> getById(@RequestParam(name = "id",required = false) long id){
		try {
			Category foundCategory = this.categoryService.findCategoryById(id);
			if(ObjectUtils.isEmpty(foundCategory)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}
			return super.success(foundCategory);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable long id){
		try {
			Category foundCategory = this.categoryService.findCategoryById(id);
			if(ObjectUtils.isEmpty(foundCategory)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}
			return super.success(foundCategory);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@PostMapping("")
	public ResponseEntity<?> createCategory(@RequestBody Map<String,Object> category){
		try {
			if (ObjectUtils.isEmpty(category)) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}
			
			CategoryDTORequest categoryDTORequest = new CategoryDTORequest(category);
			if(ObjectUtils.isEmpty(categoryDTORequest.getName())) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}
			
			Category insertCategory = this.categoryService.addNewCategory(category);
			if(!ObjectUtils.isEmpty(insertCategory)) {
				return super.success(new CategoryDTOResponse(insertCategory));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCategoryByMap(@PathVariable long id, 
			@RequestBody(required = true) Map<String,Object> category){
		try {
			if (ObjectUtils.isEmpty(category)) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}
			
			CategoryDTORequest categoryDTORequest = new CategoryDTORequest(category);
			if(ObjectUtils.isEmpty(categoryDTORequest.getName())) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}
			
			Category foundCategory = this.categoryService.findCategoryById(id);
			if(ObjectUtils.isEmpty(foundCategory)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}
			Category updateCategory = this.categoryService.updateCategory(id, category);
			return super.success(new CategoryDTOResponse(updateCategory));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable long id){
		try {
			Category foundCategory = this.categoryService.findCategoryById(id);
			if(ObjectUtils.isEmpty(foundCategory)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}
			this.categoryService.deleteCategoryById(id);
			return super.success(foundCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
}
