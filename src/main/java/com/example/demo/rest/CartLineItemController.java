package com.example.demo.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.CategoryDTORequest;
import com.example.demo.DTO.CategoryDTOResponse;
import com.example.demo.constant.ResponseCode;
import com.example.demo.model.Category;
import com.example.demo.model.VariantProduct;
import com.example.demo.model.Cart;
import com.example.demo.model.CartLineItem;
import com.example.demo.repository.CartLineItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.VariantProductRepository;



@RestController
@RequestMapping(path = "/cartLineItems")
public class CartLineItemController extends BaseRestController{
	@Autowired
	private CartLineItemRepository cartLineItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private VariantProductRepository variantProductRepository;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@PostMapping("")
	@Transactional(noRollbackFor = { Exception.class })
	public ResponseEntity<?> createCartLineItem(@RequestBody Map<String,Object> newCartLineItem){
		try {
			if (ObjectUtils.isEmpty(newCartLineItem)||ObjectUtils.isEmpty(newCartLineItem.get("quantity"))
				 ||ObjectUtils.isEmpty(newCartLineItem.get("cartId"))
				 ||ObjectUtils.isEmpty(newCartLineItem.get("variantProductId"))) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}
			
			Long cartId = Long.parseLong(newCartLineItem.get("cartId").toString());
			Cart foundCart = this.cartRepository.findById(cartId).orElse(null);
			Long variantProductId = Long.parseLong(newCartLineItem.get("variantProductId").toString());
			VariantProduct foundVariantProductId = this.variantProductRepository.findById(variantProductId).orElse(null);
			if(ObjectUtils.isEmpty(foundCart) || ObjectUtils.isEmpty(foundVariantProductId)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}
			
			CartLineItem foundVariantProductInCartLineItem = this.cartLineItemRepository
							.findByVariantProductIdAndCartId(variantProductId,cartId);
			if(!ObjectUtils.isEmpty(foundVariantProductInCartLineItem)) {
				return super.error(ResponseCode.DATA_ALREADY_EXISTS.getCode(), ResponseCode.DATA_ALREADY_EXISTS.getMessage());
			}
			
			CartLineItem insertCategory = new CartLineItem();
			insertCategory.setQuantity(Integer.parseInt(newCartLineItem.get("quantity").toString()));
			insertCategory.setCart(foundCart);
			insertCategory.setVariantProduct(foundVariantProductId);
			this.cartLineItemRepository.save(insertCategory);
			return super.success(insertCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
}
