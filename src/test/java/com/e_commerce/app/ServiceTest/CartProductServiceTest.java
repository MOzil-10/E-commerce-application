package com.e_commerce.app.ServiceTest;

import com.e_commerce.app.model.CartProduct;
import com.e_commerce.app.repository.CartProductRepository;
import com.e_commerce.app.service.CartProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartProductServiceTest {

    @InjectMocks
    private CartProductService cartProductService;

    @Mock
    private CartProductRepository cartProductRepository;

    private CartProduct cartProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartProduct = new CartProduct(1, null, null);
    }

    @Test
    void testAddCartProduct() {
        when(cartProductRepository.save(cartProduct)).thenReturn(cartProduct);

        CartProduct result = cartProductService.addCartProduct(cartProduct);

        assertNotNull(result);
        assertEquals(cartProduct, result);
        verify(cartProductRepository, times(1)).save(cartProduct);
    }

    @Test
    void testGetCartProductById() {
        when(cartProductRepository.findById(1)).thenReturn(Optional.of(cartProduct));

        CartProduct result = cartProductService.getCartProductById(1);

        assertNotNull(result);
        assertEquals(cartProduct, result);
    }

    @Test
    void testGetCartProductById_NotFound() {
        when(cartProductRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartProductService.getCartProductById(1);
        });

        assertEquals("CartProduct not found with id: 1", exception.getMessage());
    }

    @Test
    void testUpdateCartProduct() {
        CartProduct updatedCartProduct = new CartProduct(1, null, null);
        when(cartProductRepository.findById(1)).thenReturn(Optional.of(cartProduct));
        when(cartProductRepository.save(updatedCartProduct)).thenReturn(updatedCartProduct);

        CartProduct result = cartProductService.updateCartProduct(1, updatedCartProduct);

        assertNotNull(result);
        assertEquals(updatedCartProduct, result);
        verify(cartProductRepository, times(1)).save(updatedCartProduct);
    }

    @Test
    void testDeleteCartProduct() {
        when(cartProductRepository.existsById(1)).thenReturn(true);

        cartProductService.deleteCartProduct(1);

        verify(cartProductRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteCartProduct_NotFound() {
        when(cartProductRepository.existsById(1)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartProductService.deleteCartProduct(1);
        });

        assertEquals("CartProduct with id 1 does not exist", exception.getMessage());
    }
}
