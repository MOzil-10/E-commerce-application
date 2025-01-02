package com.e_commerce.app.service;

import com.e_commerce.app.model.CartProduct;
import com.e_commerce.app.repository.CartProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartProductService {

    private final CartProductRepository repository;

    public CartProductService(CartProductRepository repository) {
        this.repository = repository;
    }

    public CartProduct addCartProduct(CartProduct cartProduct) {
        return repository.save(cartProduct);
    }

    public List<CartProduct> getAllCartProducts() {
        return repository.findAll();
    }

    public CartProduct getCartProductById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartProduct not found with id: " + id));
    }

    public CartProduct updateCartProduct(int id, CartProduct cartProduct) {
        CartProduct existingCartProduct = getCartProductById(id);
        existingCartProduct.setCart(cartProduct.getCart());
        existingCartProduct.setProduct(cartProduct.getProduct());
        return repository.save(existingCartProduct);
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    public void deleteCartProduct(int id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("CartProduct with id " + id + " does not exist");
        }
        repository.deleteById(id);
    }
}
