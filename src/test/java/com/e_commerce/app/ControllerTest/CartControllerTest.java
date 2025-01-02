package com.e_commerce.app.ControllerTest;

import com.e_commerce.app.controller.CartController;
import com.e_commerce.app.model.Cart;
import com.e_commerce.app.model.CartProduct;
import com.e_commerce.app.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private Cart cart;
    private CartProduct cartProduct;

    @BeforeEach
    void setUp() {
        cart = new Cart(1, null, new ArrayList<>());
        cartProduct = new CartProduct();
    }

    @Test
    void createCart_shouldReturnCreatedCart() {
        when(cartService.createCart(cart)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.createCart(cart);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(cart, response.getBody());
    }

    @Test
    void getAllCarts_shouldReturnCartsList() {
        List<Cart> carts = new ArrayList<>();
        carts.add(cart);
        when(cartService.getAllCarts()).thenReturn(carts);

        ResponseEntity<List<Cart>> response = cartController.getAllCarts();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getCartById_shouldReturnCart() {
        when(cartService.getCartById(1)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.getCartById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(cart, response.getBody());
    }

    @Test
    void getCartById_shouldReturnNotFound() {
        when(cartService.getCartById(1)).thenThrow(new RuntimeException("Cart not found"));

        ResponseEntity<Cart> response = cartController.getCartById(1);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void updateCart_shouldReturnUpdatedCart() {
        Cart updatedCart = new Cart(1, null, new ArrayList<>());
        when(cartService.updateCart(1, updatedCart)).thenReturn(updatedCart);

        ResponseEntity<Cart> response = cartController.updateCart(1, updatedCart);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(updatedCart, response.getBody());
    }

    @Test
    void deleteCart_shouldReturnNoContent() {
        doNothing().when(cartService).deleteCart(1);

        ResponseEntity<Void> response = cartController.deleteCart(1);

        assertEquals(204, response.getStatusCodeValue());
    }

//    @Test
//    void addProductToCart_shouldReturnNoContent() {
//        when(cartService.addProductToCart(1, cartProduct)).thenReturn(null);
//
//        ResponseEntity<Void> response = cartController.addProductToCart(1, cartProduct);
//
//        assertEquals(204, response.getStatusCodeValue());
//    }

    @Test
    void removeProductFromCart_shouldReturnNoContent() {
        doNothing().when(cartService).removeProductFromCart(1, 1);

        ResponseEntity<Void> response = cartController.removeProductFromCart(1, 1);

        assertEquals(204, response.getStatusCodeValue());
    }
}
