package com.cosmetics.product.controller;

import com.cosmetics.product.dto.request.BrandRequest;
import com.cosmetics.product.dto.request.CategoryRequest;
import com.cosmetics.product.entity.Brand;
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
@RequestMapping("/brands")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {

    BrandService brandService;

    @PostMapping("/create-brand")
    @Operation(summary = "Tạo thương hiệu sản phẩm")
    public void createBrand(
            @Valid @RequestBody BrandRequest brandRequest) {
        brandService.createBrand(brandRequest);
    }

    @GetMapping("/get-all-brands")
    @Operation(summary = "Lấy danh sách brand")
    public ResponseEntity<List<Brand>> getAllBrands(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 100;
        }
        List<Brand> brands = brandService.getAllBrands(page, limit);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy brand bằng id")
    public ResponseEntity<Brand> getCategoryById(@PathVariable("id") String id) {
        Brand brand = brandService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật brand")
    public void updateBrands(
            @PathVariable String id,
            @Valid @RequestBody BrandRequest brandDTO
    ) {
        brandService.updateBrand(id, brandDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa brand")
    public void deleteCategories(@PathVariable String id) {
        brandService.deleteBrand(id);
    }
}
