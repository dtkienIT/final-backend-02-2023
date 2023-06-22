package com.example.demo.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.ResponseCode;
import com.example.demo.model.User;
import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends BaseRestController{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;
	
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping("/{id}")
	@Transactional(noRollbackFor = { Exception.class })
	public ResponseEntity<?> updateUserByMap(@PathVariable long id,
			@RequestBody(required = false) Map<String, Object> newUser) {
		try {
			if (ObjectUtils.isEmpty(newUser)) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			if (ObjectUtils.isEmpty(newUser.get("phone")) || ObjectUtils.isEmpty(newUser.get("email"))
					||ObjectUtils.isEmpty(newUser.get("displayName"))||ObjectUtils.isEmpty(newUser.get("name"))
					|| ObjectUtils.isEmpty(newUser.get("password"))) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			User foundUser = this.userRepository.findById(id).orElse(null);
			if (ObjectUtils.isEmpty(foundUser)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}

			foundUser.setName(newUser.get("name").toString());
			foundUser.setPhone(newUser.get("phone").toString());
			foundUser.setEmail(newUser.get("email").toString());
			foundUser.setDisplayName(newUser.get("displayName").toString());
			foundUser.setPassword(this.passwordEncoder.encode(newUser.get("password").toString()));
			this.userRepository.save(foundUser);
			return super.success(foundUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("")
	@Transactional(noRollbackFor = { Exception.class })
	public ResponseEntity<?> addUser(@RequestBody(required = true) Map<String, Object> newUser) {
		try {
			if (ObjectUtils.isEmpty(newUser)) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			if (ObjectUtils.isEmpty(newUser.get("name")) || ObjectUtils.isEmpty(newUser.get("phone"))
					|| ObjectUtils.isEmpty(newUser.get("password")) || ObjectUtils.isEmpty(newUser.get("email"))
					|| ObjectUtils.isEmpty(newUser.get("displayName"))) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			User foundUser = this.userRepository.findByName(newUser.get("name").toString()).orElse(null);
			if (!ObjectUtils.isEmpty(foundUser)) {
				return super.error(ResponseCode.DATA_ALREADY_EXISTS.getCode(),
						ResponseCode.DATA_ALREADY_EXISTS.getMessage());
			}

			User insertedUser = new User();
			insertedUser.setName(newUser.get("name").toString());
			insertedUser.setEmail(newUser.get("email").toString());
			insertedUser.setPhone(newUser.get("phone").toString());
			insertedUser.setDisplayName(newUser.get("displayName").toString());
			insertedUser.setPassword(this.passwordEncoder.encode(newUser.get("password").toString()));
			insertedUser.setRoles(this.roleRepository.findByRoleName("USER"));

			Cart cart = new Cart();
		    cart.setUser(insertedUser);
		    this.cartRepository.save(cart);
		    
			this.userRepository.save(insertedUser);
			if (!ObjectUtils.isEmpty(insertedUser)) {
				return super.success(insertedUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable long id) {
		try {
			User foundUser = this.userRepository.findById(id).orElse(null);
			if (ObjectUtils.isEmpty(foundUser)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}
			return super.success(foundUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
}
