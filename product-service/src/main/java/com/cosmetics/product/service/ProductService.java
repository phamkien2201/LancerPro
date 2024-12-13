package com.cosmetics.product.service;

import com.cosmetics.product.dto.request.ProductRequest;
import com.cosmetics.product.dto.response.ProductListResponse;
import com.cosmetics.product.dto.response.ProductResponse;
import com.cosmetics.product.entity.Brand;
import com.cosmetics.product.entity.Category;
import com.cosmetics.product.entity.Product;
import com.cosmetics.product.exception.AppException;
import com.cosmetics.product.exception.ErrorCode;
import com.cosmetics.product.mapper.ProductMapper;
import com.cosmetics.product.repository.BrandRepository;
import com.cosmetics.product.repository.CategoryRepository;
import com.cosmetics.product.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    BrandRepository brandRepository;

    public ProductResponse createProduct(ProductRequest request){
        Product product = productMapper.toProduct(request);
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository
                .findAll(pageRequest)
                .map(productMapper::toProductResponse);
    }

    public ProductResponse getProductById(String id) {
       Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cannot found product with id "+id));
        return productMapper.toProductResponse(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProduct(String productId, ProductRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        productMapper.updateProduct(product, request);
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(String id) {
        productRepository.findById(id).ifPresent(productRepository::delete);
    }


    public ProductListResponse findProductsByCategoryId(String categoryId, int page, int limit, String sortBy, String sortDirection) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("No category found with id: " + categoryId);
        }

        List<Category> subCategories = categoryRepository.findByParentId(categoryId);
        Page<Product> products;
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sortBy));

        if (subCategories.isEmpty()) {
            products = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            List<String> categoryIds = findAllSubCategoryIds(categoryId);
            products = productRepository.findByCategoryIdIn(categoryIds, pageable);
        }

        List<ProductResponse> productResponses = products.map(productMapper::toProductResponse).getContent();
        int totalPages = products.getTotalPages();

        return ProductListResponse.builder()
                .products(productResponses)
                .totalPages(totalPages)
                .build();
    }

    private List<String> findAllSubCategoryIds(String parentId) {
        List<String> categoryIds = new ArrayList<>();
        categoryIds.add(parentId); // Thêm chính category lớn vào danh sách
        List<Category> subCategories = categoryRepository.findByParentId(parentId);
        for (Category subCategory : subCategories) {
            categoryIds.add(subCategory.getId());
            categoryIds.addAll(findAllSubCategoryIds(subCategory.getId())); // Recursive
        }
        return categoryIds;
    }

    public ProductListResponse findProductsByBrandId(String brandId, int page, int limit, String sortBy, String sortDirection) {
        brandRepository.findById(brandId).orElseThrow(() ->
                new IllegalArgumentException("Cannot find brand with id: " + brandId)
        );
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sortBy));
        Page<Product> products = productRepository.findByBrandId(brandId, pageable);

        List<ProductResponse> productResponses = products.map(productMapper::toProductResponse).getContent();
        int totalPages = products.getTotalPages();

        return ProductListResponse.builder()
                .products(productResponses)
                .totalPages(totalPages)
                .build();
    }

    public void updateProductQuantity(String productId, Float newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        product.setQuantity(newQuantity);
        productRepository.save(product);
    }


}
