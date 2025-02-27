package com.lancer.project.repository;

import com.lancer.project.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByParentId(String parentId);
    List<Category> findByNameContainingIgnoreCase(String name);
}
