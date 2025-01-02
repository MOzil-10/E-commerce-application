package com.e_commerce.app.ControllerTest;

import com.e_commerce.app.controller.CartProductController;
import com.e_commerce.app.model.CartProduct;
import com.e_commerce.app.service.CartProductService;
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
public class CartProductControllerTest {

    @Mock
    private CartProductService cartProductService;

    @InjectMocks
    private CartProductController cartProductController;

    private CartProduct cartProduct;

    @BeforeEach
    void setUp() {
        cartProduct = new CartProduct();
    }

    @Test
    void createCartProduct_shouldReturnCreatedCartProduct() {
        when(cartProductService.addCartProduct(cartProduct)).thenReturn(cartProduct);

        ResponseEntity<CartProduct> response = cartProductController.createCartProduct(cartProduct);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(cartProduct, response.getBody());
    }

    @Test
    void getAllCartProducts_shouldReturnCartProductsList() {
        List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(cartProduct);
        when(cartProductService.getAllCartProducts()).thenReturn(cartProducts);

        ResponseEntity<List<CartProduct>> response = cartProductController.getAllCartProducts();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getCartProductById_shouldReturnCartProduct() {
        when(cartProductService.getCartProductById(1)).thenReturn(cartProduct);

        ResponseEntity<CartProduct> response = cartProductController.getCartProductById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(cartProduct, response.getBody());
    }

    @Test
    void getCartProductById_shouldReturnNotFound() {
        when(cartProductService.getCartProductById(1)).thenThrow(new RuntimeException("CartProduct not found"));

        ResponseEntity<CartProduct> response = cartProductController.getCartProductById(1);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void updateCartProduct_shouldReturnUpdatedCartProduct() {
        CartProduct updatedCartProduct = new CartProduct();
        when(cartProductService.updateCartProduct(1, updatedCartProduct)).thenReturn(updatedCartProduct);

        ResponseEntity<CartProduct> response = cartProductController.updateCartProduct(1, updatedCartProduct);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(updatedCartProduct, response.getBody());
    }

    @Test
    void deleteCartProduct_shouldReturnNoContent() {
        doNothing().when(cartProductService).deleteCartProduct(1);

        ResponseEntity<Void> response = cartProductController.deleteCartProduct(1);

        assertEquals(204, response.getStatusCodeValue());
    }
}
