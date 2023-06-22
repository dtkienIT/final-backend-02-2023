package com.example.demo.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.CategoryDTOResponse;
import com.example.demo.constant.ResponseCode;
import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.model.Category;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/addresses")
public class AddressController extends BaseRestController{
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/{userId}")
	public ResponseEntity<?> getAdressByUserId(@PathVariable Long userId){
		try {
			User foundUser = this.userRepository.findById(userId).orElse(null);
			if(ObjectUtils.isEmpty(foundUser)) {
				return super.error(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
			}
			List<Address> address = this.addressRepository.findAllAddressesByUserId(userId);
			return super.success(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole( 'USER')")
	@PostMapping("")
	public ResponseEntity<?> addAdress(@RequestBody Map<String,Object> newAddress){
		try {
			Long userId = Long.parseLong(newAddress.get("userId").toString());
			User foundUser = this.userRepository.findById(userId).orElse(null);
			if(ObjectUtils.isEmpty(foundUser)) {
				return super.error(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
			}
			
			if(ObjectUtils.isEmpty(newAddress)||ObjectUtils.isEmpty(newAddress.get("street")) 
			|| ObjectUtils.isEmpty(newAddress.get("city"))|| ObjectUtils.isEmpty(newAddress.get("country"))) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}
		
			Address insertAddress = new Address();
			insertAddress.setCity(newAddress.get("city").toString());
			insertAddress.setCountry(newAddress.get("country").toString());
			insertAddress.setStreet(newAddress.get("street").toString());
			insertAddress.setUser(foundUser);
			this.addressRepository.save(insertAddress);
			return super.success(insertAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateAdress(@PathVariable Long id,@RequestBody Map<String,Object> newAddress){
		try {
			Long userId = Long.parseLong(newAddress.get("userId").toString());
			User foundUser = this.userRepository.findById(userId).orElse(null);
			if(ObjectUtils.isEmpty(foundUser)) {
				return super.error(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
			}
			
			if(ObjectUtils.isEmpty(newAddress)||ObjectUtils.isEmpty(newAddress.get("street")) 
			|| ObjectUtils.isEmpty(newAddress.get("city"))|| ObjectUtils.isEmpty(newAddress.get("country"))) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}
			
			Address foundAddress = this.addressRepository.findById(id).orElse(null);
			if(ObjectUtils.isEmpty(foundAddress)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}
			
			Address updateAddress = new Address();
			updateAddress.setCity(newAddress.get("city").toString());
			updateAddress.setCountry(newAddress.get("country").toString());
			updateAddress.setStreet(newAddress.get("street").toString());
			updateAddress.setUser(foundUser);
			updateAddress.setId(id);
			this.addressRepository.save(updateAddress);
			return super.success(updateAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAdress(@PathVariable long id){
		try {		
			Address foundAddress = this.addressRepository.findById(id).orElse(null);
			if(ObjectUtils.isEmpty(foundAddress)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}
			this.addressRepository.deleteById(id);
			return super.success(foundAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
}
