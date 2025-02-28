package com.lancer.project.service;

import com.lancer.project.dto.request.CategoryRequest;
import com.lancer.project.entity.Category;
import com.lancer.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHit;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryService {

    CategoryRepository categoryRepository;
    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @PreAuthorize("hasRole('ADMIN')")
    public void createCategory(CategoryRequest categoryDTO) {
        Category newCategory = Category
                .builder()
                .name(categoryDTO.getName())
                .parentId(categoryDTO.getParentId())
                .build();
        categoryRepository.save(newCategory);
        elasticsearchOperations.save(newCategory);
    }

    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public List<Category> getAllCategories(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);
        return categoryPage.getContent();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(String categoryId,
                               CategoryRequest categoryDTO) {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setId(categoryDTO.getParentId());
        categoryRepository.save(existingCategory);
        elasticsearchOperations.save(existingCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
        elasticsearchOperations.delete(id, Category.class);
    }

    public List<Category> findCategoryByParentId(String parentId) {
        return categoryRepository.findByParentId(parentId);
    }
    public List<Category> findCategoryByName(String name) {
        Criteria criteria = new Criteria("name").contains(name);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<Category> searchHits = elasticsearchOperations.search(query, Category.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .toList();
    }


}
