package com.e_commerce.app.ServiceTest;

import com.e_commerce.app.model.Category;
import com.e_commerce.app.model.Product;
import com.e_commerce.app.repository.ProductRepository;
import com.e_commerce.app.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProductWithUniqueName() {
        Category category = new Category(1, "Electronics", null);
        Product product = new Product(0, "Laptop", category, 10, 1000.0, "A high-quality laptop");

        when(productRepository.getProductByName(product.getName())).thenReturn(Optional.empty());
        when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        verify(productRepository, times(1)).getProductByName(product.getName());
        verify(productRepository, times(1)).save(product);

        assertNotNull(createdProduct);
        assertEquals(product.getName(), createdProduct.getName());
        assertEquals(product.getCategory(), createdProduct.getCategory());
        assertEquals(product.getPrice(), createdProduct.getPrice());
    }

    @Test
    void testCreateProductWithDuplicateName() {
        Category category = new Category(1, "Electronics", null);
        Product product = new Product(0, "Laptop", category, 10, 1000.0, "A high-quality laptop");

        when(productRepository.getProductByName(product.getName())).thenReturn(Optional.of(product));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.createProduct(product));

        verify(productRepository, times(1)).getProductByName(product.getName());
        assertEquals("Product with name Laptop already exists", exception.getMessage());
    }

    @Test
    void testGetAllProducts() {
        productService.getAllProducts();

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProduct() {
        Category category = new Category(1, "Electronics", null);
        Product existingProduct = new Product(1, "Laptop", category, 10, 1000.0, "Old description");
        Product updatedProduct = new Product(1, "Updated Laptop", category, 5, 1200.0, "Updated description");

        when(productRepository.findById(existingProduct.getId())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(existingProduct.getId(), updatedProduct);

        verify(productRepository, times(1)).findById(existingProduct.getId());
        verify(productRepository, times(1)).save(any(Product.class));

        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        assertEquals(updatedProduct.getQuantity(), result.getQuantity());
    }

    @Test
    void testDeleteProduct() {
        int productId = 1;

        when(productRepository.existsById(productId)).thenReturn(true);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProductNotFound() {
        int productId = 999;

        when(productRepository.existsById(productId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.deleteProduct(productId));

        verify(productRepository, times(1)).existsById(productId);
        assertEquals("Product with id 999 does not exist", exception.getMessage());
    }
}
