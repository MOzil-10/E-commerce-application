package com.e_commerce.app.service;

import com.e_commerce.app.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository repository;

    public CartService(CartRepository repository){
        this.repository = repository;
    }
}
