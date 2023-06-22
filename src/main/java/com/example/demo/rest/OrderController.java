package com.example.demo.rest;


import java.util.Date;

import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.ResponseCode;
import com.example.demo.model.Address;
import com.example.demo.model.CartLineItem;
import com.example.demo.model.User;
import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/orders")
public class OrderController extends BaseRestController{
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping("")
	@Transactional(noRollbackFor = { Exception.class })
	public ResponseEntity<?> createOrder(@RequestBody Map<String,Object> newOrder){
		try {
			if (ObjectUtils.isEmpty(newOrder) || ObjectUtils.isEmpty(newOrder.get("deliveryTime"))
			|| ObjectUtils.isEmpty(newOrder.get("totalAmount"))|| ObjectUtils.isEmpty(newOrder.get("userId"))
			||ObjectUtils.isEmpty(newOrder.get("shippingAddressId"))) {
			return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
		}
			
			Long userId = Long.parseLong(newOrder.get("userId").toString());
			User foundUser = this.userRepository.findById(userId).orElse(null);
			if(ObjectUtils.isEmpty(foundUser)) {
				return super.error(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
			}
			
			Long addressId = Long.parseLong(newOrder.get("shippingAddressId").toString());
			Address foundAddress = this.addressRepository.findById(addressId).orElse(null);
			if(ObjectUtils.isEmpty(foundAddress)) {
				return super.error(ResponseCode.ADDRESS_NOT_FOUND.getCode(), ResponseCode.ADDRESS_NOT_FOUND.getMessage());
			}
			
			Cart cart = this.cartRepository.findOneByUserId(userId);
			if (cart == null) {
			    return super.error(ResponseCode.CART_NOT_FOUND.getCode(), ResponseCode.CART_NOT_FOUND.getMessage());
			}
			Long cartId = cart.getId();
			List<CartLineItem> cartLineItem = this.orderRepository.findAllCartLineItemsByCartId(cartId);
			if (cartLineItem.isEmpty()) {
			    return super.error(ResponseCode.CART_LINE_ITEMS_NOT_FOUND.getCode(),
			    		ResponseCode.CART_LINE_ITEMS_NOT_FOUND.getMessage());
			}
			for (CartLineItem item : cartLineItem) {
			    item.setDeleted(true);
			}
			
			Order order = new Order();
			String deliveryTimeStr = newOrder.get("deliveryTime").toString();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date deliveryTime;
			try {
			    deliveryTime = dateFormat.parse(deliveryTimeStr);
			} catch (Exception e) {
			    return super.error(ResponseCode.INVALID_DATE_FORMAT.getCode(), ResponseCode.INVALID_DATE_FORMAT.getMessage());
			}
			
			order.setDeliveryTime(deliveryTime);
			order.setOrderDate(new Date());
			order.setTotalAmount(Double.parseDouble(newOrder.get("totalAmount").toString()));
			order.setUser(foundUser);
			order.setAddress(foundAddress);
			this.orderRepository.save(order);
			return super.success(newOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
}
