package com.cosmetics.product.repository;

import com.cosmetics.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    Page<Product> findByCategoryId(String categoryId, Pageable pageable);
    Page<Product> findByCategoryIdIn(List<String> categoryIds, Pageable pageable);
    Page<Product> findByBrandId(String brandId, Pageable pageable);
}
