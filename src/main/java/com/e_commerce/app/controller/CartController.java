package com.e_commerce.app.controller;

import com.e_commerce.app.model.Cart;
import com.e_commerce.app.model.CartProduct;
import com.e_commerce.app.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        try {
            Cart createdCart = service.createCart(cart);
            return ResponseEntity.ok(createdCart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = service.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable int id) {
        try {
            Cart cart = service.getCartById(id);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable int id, @RequestBody Cart cart) {
        try {
            Cart updatedCart = service.updateCart(id, cart);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable int id) {
        try {
            service.deleteCart(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{cartId}/product")
    public ResponseEntity<Void> addProductToCart(@PathVariable int cartId, @RequestBody CartProduct cartProduct) {
        try {
            service.addProductToCart(cartId, cartProduct);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{cartId}/product/{cartProductId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable int cartId, @PathVariable int cartProductId) {
        try {
            service.removeProductFromCart(cartId, cartProductId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
