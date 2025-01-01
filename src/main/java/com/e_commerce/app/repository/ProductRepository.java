package com.e_commerce.app.repository;

import com.e_commerce.app.model.Category;
import com.e_commerce.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> getProductByName(String name);
    Optional<Category> getProductByCategory(Category category);
}
