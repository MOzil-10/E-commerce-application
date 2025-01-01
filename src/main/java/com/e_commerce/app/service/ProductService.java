package com.e_commerce.app.service;

import com.e_commerce.app.model.Category;
import com.e_commerce.app.model.Product;
import com.e_commerce.app.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product createProduct(Product product) {

        Optional<Product> existingProduct = repository.getProductByName(product.getName());
        if (existingProduct.isPresent()) {
            throw new RuntimeException("Product with name " + product.getName() + " already exists");
        }
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Optional<Category> getProductsByCategory(Category category) {
        return repository.getProductByCategory(category);
    }

    public Product updateProduct(int id, Product product) {
        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with id " + id + " does not exist"));

        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());

        return repository.save(existingProduct);
    }

    public void deleteProduct(int id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Product with id " + id + " does not exist");
        }
        repository.deleteById(id);
    }
}
