package com.e_commerce.app.ServiceTest;

import com.e_commerce.app.model.Category;
import com.e_commerce.app.repository.CategoryRepository;
import com.e_commerce.app.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategoryWithUniqueName() {
        Category category = new Category();
        category.setName("Electronics");

        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(category)).thenReturn(category);

        Category createdCategory = categoryService.createCategory(category);

        verify(categoryRepository, times(1)).findByName(category.getName());
        verify(categoryRepository, times(1)).save(category);

        assertNotNull(createdCategory);
        assertEquals(category.getName(), createdCategory.getName());
    }

    @Test
    void testCreateCategoryWithDuplicateName() {
        Category category = new Category();
        category.setName("Electronics");

        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.createCategory(category);
        });

        verify(categoryRepository, times(1)).findByName(category.getName());
        assertEquals("Error creating category: Category with the name 'Electronics' already exists", exception.getMessage());
    }

    @Test
    void testGetAllCategories() {
        categoryService.getAllCategories();

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById() {
        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getCategoryById(1);

        verify(categoryRepository, times(1)).findById(1);
        assertEquals(category.getName(), foundCategory.getName());
    }

    @Test
    void testGetCategoryByIdNotFound() {
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.getCategoryById(999);
        });

        verify(categoryRepository, times(1)).findById(999);
        assertEquals("Category not found with id: 999", exception.getMessage());
    }

    @Test
    void testUpdateCategory() {
        Category existingCategory = new Category();
        existingCategory.setId(1);
        existingCategory.setName("Electronics");

        Category updatedCategory = new Category();
        updatedCategory.setName("Home Appliances");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        Category result = categoryService.updateCategory(1, updatedCategory);

        verify(categoryRepository, times(1)).findById(1);
        verify(categoryRepository, times(1)).save(existingCategory);

        assertEquals(updatedCategory.getName(), result.getName());
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category();
        category.setId(1);

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1);

        verify(categoryRepository, times(1)).findById(1);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void testDeleteCategoryNotFound() {
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.deleteCategory(999);
        });

        verify(categoryRepository, times(1)).findById(999);
        assertEquals("Category not found with id: 999", exception.getMessage());
    }
}
