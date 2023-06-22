package com.example.demo.security;

import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class CustomUserServiceDetail implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// can phai sua lai model, username phai unique, va findByName return optional
		Optional<User> user = this.userRepository.findByName(username);
		if (user.isPresent()) {
			return new CustomUserDetail(user.get());
		} else {
			throw new RuntimeException("User not found");
		}
	}
}
