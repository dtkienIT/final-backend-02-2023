package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByName(String name);

}
