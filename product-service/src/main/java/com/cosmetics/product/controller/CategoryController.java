package com.cosmetics.product.controller;

import com.cosmetics.product.dto.request.CategoryRequest;
import com.cosmetics.product.entity.Category;
import com.cosmetics.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping("/create-category")
    @Operation(summary = "Tạo danh mục sản phẩm")
    public void createCategories(
            @Valid @RequestBody CategoryRequest categoryDTO) {
        categoryService.createCategory(categoryDTO);
    }

    @GetMapping("/get-all-categories")
    @Operation(summary = "Lấy tất cả danh mục sản phâm")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false ) Integer limit
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 150;
        }
        List<Category> categories = categoryService.getAllCategories(page, limit);
        return ResponseEntity.ok(categories);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Cập nhat danh muc")
    public void updateCategories(
            @PathVariable String id,
            @Valid @RequestBody CategoryRequest categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa danh mục")
    public void deleteCategories(@PathVariable String id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/parent/{parentId}")
    @Operation(summary = "Tìm danh mục bằng parentId")
    public ResponseEntity<List<Category>> findCategoriesByParentId(@PathVariable String parentId) {
        List<Category> categories = categoryService.findCategoryByParentId(parentId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy danh muc bằng id")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") String id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
}
