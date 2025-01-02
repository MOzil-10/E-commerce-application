package com.e_commerce.app.ControllerTest;

import com.e_commerce.app.controller.ProductController;
import com.e_commerce.app.model.Category;
import com.e_commerce.app.model.Product;
import com.e_commerce.app.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        category = new Category(1, "Electronics", Collections.emptyList());
        product = new Product(1, "Laptop", category, 10, 999.99, "A high-end laptop");
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Laptop\", \"category\": {\"id\": 1}, \"quantity\": 10, \"price\": 999.99, \"description\": \"A high-end laptop\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.price").value(999.99));
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product product1 = new Product(1, "Laptop", category, 10, 999.99, "A high-end laptop");
        Product product2 = new Product(2, "Smartphone", category, 20, 499.99, "A new smartphone");
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].name").value("Laptop"))
                .andExpect(jsonPath("[1].name").value("Smartphone"));
    }

    @Test
    void testGetAllProductsEmpty() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/product/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product updatedProduct = new Product(1, "Updated Laptop", category, 15, 1099.99, "An updated version of the laptop");
        when(productService.updateProduct(anyInt(), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Laptop\", \"category\": {\"id\": 1}, \"quantity\": 15, \"price\": 1099.99, \"description\": \"An updated version of the laptop\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.price").value(1099.99));
    }

    @Test
    void testUpdateProductNotFound() throws Exception {
        when(productService.updateProduct(anyInt(), any(Product.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Laptop\", \"category\": {\"id\": 1}, \"quantity\": 15, \"price\": 1099.99, \"description\": \"An updated version of the laptop\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1);

        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProductNotFound() throws Exception {
        doThrow(new RuntimeException()).when(productService).deleteProduct(1);

        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isNotFound());
    }
}
