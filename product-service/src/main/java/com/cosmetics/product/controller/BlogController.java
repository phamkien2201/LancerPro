package com.cosmetics.product.controller;

import com.cosmetics.product.dto.request.BlogRequest;
import com.cosmetics.product.dto.request.BrandRequest;
import com.cosmetics.product.entity.Blog;
import com.cosmetics.product.entity.Brand;
import com.cosmetics.product.service.BlogService;
import com.cosmetics.product.service.BrandService;
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
@RequestMapping("/blogs")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {

    BlogService blogService;

    @PostMapping("/create-blog")
    @Operation(summary = "Tạo thương hiệu sản phẩm")
    public void createBlog(
            @Valid @RequestBody BlogRequest request) {
        blogService.createBlog(request);
    }

    @GetMapping("/get-all-brands")
    @Operation(summary = "Lấy danh sách brand")
    public ResponseEntity<List<Blog>> getAllBlogs(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 100;
        }
        List<Blog> blogs = blogService.getAllBlogs(page, limit);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy blog bằng id")
    public ResponseEntity<Blog> getBlogById(@PathVariable("id") String id) {
        Blog blog = blogService.getBlogById(id);
        return ResponseEntity.ok(blog);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật")
    public void updateBlog(
            @PathVariable String id,
            @Valid @RequestBody BlogRequest request
    ) {
        blogService.updateBlog(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa blog")
    public void deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
    }
}
