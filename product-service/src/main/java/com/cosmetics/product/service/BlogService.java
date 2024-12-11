package com.cosmetics.product.service;

import com.cosmetics.product.dto.request.BlogRequest;
import com.cosmetics.product.entity.Blog;
import com.cosmetics.product.repository.BlogRepository;
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
public class BlogService {

    BlogRepository blogRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public void createBlog(BlogRequest request) {
        Blog newblog = Blog
                .builder()
                .title(request.getTitle())
                .thumbnails(request.getThumbnails())
                .description(request.getDescription())
                .build();
        blogRepository.save(newblog);
    }

    public Blog getBlogById(String id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
    }

    public List<Blog> getAllBlogs(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Blog> brandPage = blogRepository.findAll(pageRequest);
        return brandPage.getContent();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateBlog(String brandId, BlogRequest brandDTO) {
        Blog existingBrand = getBlogById(brandId);
        existingBrand.setTitle(brandDTO.getTitle());
        existingBrand.setDescription(brandDTO.getDescription());
        existingBrand.setThumbnails(brandDTO.getThumbnails());
        blogRepository.save(existingBrand);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBlog(String id) {
        blogRepository.deleteById(id);
    }


}
