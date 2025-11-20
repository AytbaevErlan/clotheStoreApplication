package com.school.storeapplication.service;

import com.school.storeapplication.domain.catalog.Product;
import com.school.storeapplication.repo.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogService {

    private final ProductRepository products;

    public CatalogService(ProductRepository products) {
        this.products = products;
    }

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return products.findAll();
    }

    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return products.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Product> getLatestProducts(int limit) {
        return products.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();
    }
}
