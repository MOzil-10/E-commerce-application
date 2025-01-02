package com.e_commerce.app.ServiceTest;

import com.e_commerce.app.model.Cart;
import com.e_commerce.app.model.CartProduct;
import com.e_commerce.app.repository.CartRepository;
import com.e_commerce.app.service.CartProductService;
import com.e_commerce.app.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartProductService cartProductService;

    private Cart cart;
    private CartProduct cartProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cart = new Cart(1, null, null); // Initialize with mock values
        cartProduct = new CartProduct(1, cart, null); // Initialize with mock values
    }

    @Test
    void testCreateCart() {
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart result = cartService.createCart(cart);

        assertNotNull(result);
        assertEquals(cart, result);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testGetCartById() {
        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartById(1);

        assertNotNull(result);
        assertEquals(cart, result);
    }

    @Test
    void testGetCartById_NotFound() {
        when(cartRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.getCartById(1);
        });

        assertEquals("Cart not found with id: 1", exception.getMessage());
    }

    @Test
    void testUpdateCart() {
        Cart updatedCart = new Cart(1, null, null);
        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(cartRepository.save(updatedCart)).thenReturn(updatedCart);

        Cart result = cartService.updateCart(1, updatedCart);

        assertNotNull(result);
        assertEquals(updatedCart, result);
        verify(cartRepository, times(1)).save(updatedCart);
    }

    @Test
    void testDeleteCart() {
        when(cartRepository.existsById(1)).thenReturn(true);

        cartService.deleteCart(1);

        verify(cartRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteCart_NotFound() {
        when(cartRepository.existsById(1)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.deleteCart(1);
        });

        assertEquals("Cart with id 1 does not exist", exception.getMessage());
    }

    @Test
    void testAddProductToCart() {
        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(cartProductService.addCartProduct(cartProduct)).thenReturn(cartProduct);
        when(cartRepository.save(cart)).thenReturn(cart);

        cartService.addProductToCart(1, cartProduct);

        assertTrue(cart.getCartProductList().contains(cartProduct));
        verify(cartProductService, times(1)).addCartProduct(cartProduct);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRemoveProductFromCart() {
        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(cartProductService.existsById(1)).thenReturn(true);

        cartService.removeProductFromCart(1, 1);

        assertFalse(cart.getCartProductList().contains(cartProduct));
        verify(cartProductService, times(1)).deleteCartProduct(1);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRemoveProductFromCart_NotFound() {
        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.removeProductFromCart(1, 1);
        });

        assertEquals("CartProduct with id 1 does not exist", exception.getMessage());
    }
}
