package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Address;
import com.example.demo.model.CartLineItem;
import com.example.demo.model.Order;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	@Query("SELECT cli FROM CartLineItem cli WHERE cli.cart.id = :cartId")
    List<CartLineItem> findAllCartLineItemsByCartId(@Param("cartId") Long cartId);
}
