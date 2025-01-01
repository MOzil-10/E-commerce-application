package com.e_commerce.app.service;

import com.e_commerce.app.model.Category;
import com.e_commerce.app.repository.CartRepository;
import com.e_commerce.app.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category createCategory(Category category) {
        try {
            Optional<Category> existingCategory = repository.findByName(category.getName());
            if (existingCategory.isPresent()) {
                throw new RuntimeException("Category with the name '" + category.getName() + "' already exists");
            }
            return repository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Error creating category: " + e.getMessage());
        }
    }

    public List<Category> getAllCategories() {
        List<Category> categories = repository.findAll();
        if (categories.isEmpty()) {
            System.out.println("No categories found in the database");
        } else {
            System.out.println("Fetched categories: " + categories);
        }
        return categories;
    }

    public Category getCategoryById(int id) {
        Optional<Category> category = repository.findById(id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }


    public Category updateCategory(int id, Category category) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(category.getName());
        return repository.save(existingCategory);
    }

    public void deleteCategory(int id) {
        Category existingCategory = getCategoryById(id);
        repository.delete(existingCategory);
    }
}
