package com.cosmetics.product.service;

import com.cosmetics.product.dto.request.CategoryRequest;
import com.cosmetics.product.entity.Category;
import com.cosmetics.product.repository.CategoryRepository;
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

    @PreAuthorize("hasRole('ADMIN')")
    public void createCategory(CategoryRequest categoryDTO) {
        Category newCategory = Category
                .builder()
                .name(categoryDTO.getName())
                .parentId(categoryDTO.getParentId())
                .build();
        categoryRepository.save(newCategory);
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
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> findCategoryByParentId(String parentId) {
        return categoryRepository.findByParentId(parentId);
    }

}
