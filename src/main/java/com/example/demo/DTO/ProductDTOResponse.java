package com.example.demo.DTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;

import com.example.demo.model.Category;
import com.example.demo.model.Product;

import lombok.Data;

@Data
public class ProductDTOResponse {
    private Long id;
    private String name;
    public ProductDTOResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
    }
}

