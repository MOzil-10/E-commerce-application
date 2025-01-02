package com.e_commerce.app.controller;

import com.e_commerce.app.model.CartProduct;
import com.e_commerce.app.service.CartProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-product")
public class CartProductController {

    private final CartProductService service;

    public CartProductController(CartProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CartProduct> createCartProduct(@RequestBody CartProduct cartProduct) {
        try {
            CartProduct createdCartProduct = service.addCartProduct(cartProduct);
            return ResponseEntity.ok(createdCartProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<CartProduct>> getAllCartProducts() {
        List<CartProduct> cartProducts = service.getAllCartProducts();
        return ResponseEntity.ok(cartProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartProduct> getCartProductById(@PathVariable int id) {
        try {
            CartProduct cartProduct = service.getCartProductById(id);
            return ResponseEntity.ok(cartProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartProduct> updateCartProduct(@PathVariable int id, @RequestBody CartProduct cartProduct) {
        try {
            CartProduct updatedCartProduct = service.updateCartProduct(id, cartProduct);
            return ResponseEntity.ok(updatedCartProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartProduct(@PathVariable int id) {
        try {
            service.deleteCartProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
