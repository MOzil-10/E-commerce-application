package com.e_commerce.app.service;

import com.e_commerce.app.model.Cart;
import com.e_commerce.app.model.CartProduct;
import com.e_commerce.app.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository repository;
    private final CartProductService cartProductService;

    public CartService(CartRepository repository, CartProductService cartProductService) {
        this.repository = repository;
        this.cartProductService = cartProductService;
    }

    public Cart createCart(Cart cart) {
        return repository.save(cart);
    }

    public List<Cart> getAllCarts() {
        return repository.findAll();
    }

    public Cart getCartById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));
    }

    public Cart updateCart(int id, Cart cart) {
        Cart existingCart = getCartById(id);
        existingCart.setCustomer(cart.getCustomer());
        existingCart.setCartProductList(cart.getCartProductList());
        return repository.save(existingCart);
    }

    public void deleteCart(int id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cart with id " + id + " does not exist");
        }
        repository.deleteById(id);
    }

    public void addProductToCart(int cartId, CartProduct cartProduct) {
        Cart cart = getCartById(cartId);
        cartProduct.setCart(cart);
        cart.getCartProductList().add(cartProductService.addCartProduct(cartProduct));
        repository.save(cart);
    }

    public void removeProductFromCart(int cartId, int cartProductId) {
        Cart cart = getCartById(cartId);
        cart.getCartProductList().removeIf(cartProduct -> cartProduct.getId() == cartProductId);
        cartProductService.deleteCartProduct(cartProductId);
        repository.save(cart);
    }
}