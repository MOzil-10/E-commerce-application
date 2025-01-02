package com.e_commerce.app.ControllerTest;

import com.e_commerce.app.controller.CategoryController;
import com.e_commerce.app.model.Category;
import com.e_commerce.app.service.CategoryService;
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

class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testCreateCategory() throws Exception {
        Category category = new Category(1, "Electronics", Collections.emptyList());
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Electronics\", \"productList\": []}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/category/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.productList").isEmpty());
    }

    @Test
    void testGetAllCategories() throws Exception {

        Category category1 = new Category(1, "Electronics", Collections.emptyList());
        Category category2 = new Category(2, "Clothing", Collections.emptyList());
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category1, category2));

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].id").value(1))
                .andExpect(jsonPath("[0].name").value("Electronics"))
                .andExpect(jsonPath("[1].id").value(2))
                .andExpect(jsonPath("[1].name").value("Clothing"));
    }

    @Test
    void testGetAllCategoriesEmpty() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/category"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = new Category(1, "Electronics", Collections.emptyList());
        when(categoryService.getCategoryById(1)).thenReturn(category);

        mockMvc.perform(get("/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.productList").isEmpty());
    }

    @Test
    void testGetCategoryByIdNotFound() throws Exception {
        when(categoryService.getCategoryById(anyInt())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/category/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category category = new Category(1, "Updated Category", Collections.emptyList());
        when(categoryService.updateCategory(anyInt(), any(Category.class))).thenReturn(category);

        mockMvc.perform(put("/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Category\", \"productList\": []}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Category"))
                .andExpect(jsonPath("$.productList").isEmpty());
    }

    @Test
    void testUpdateCategoryNotFound() throws Exception {
        when(categoryService.updateCategory(anyInt(), any(Category.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Category\", \"productList\": []}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1);

        mockMvc.perform(delete("/category/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCategoryNotFound() throws Exception {
        doThrow(new RuntimeException()).when(categoryService).deleteCategory(1);

        mockMvc.perform(delete("/category/1"))
                .andExpect(status().isNotFound());
    }
}

