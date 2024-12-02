package com.cosmetics.product.service;

import com.cosmetics.product.dto.request.BrandRequest;
import com.cosmetics.product.dto.request.CategoryRequest;
import com.cosmetics.product.entity.Brand;
import com.cosmetics.product.entity.Category;
import com.cosmetics.product.repository.BrandRepository;
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
public class BrandService {

    BrandRepository brandRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public void createBrand(BrandRequest brandRequest) {
        Brand newBrand = Brand
                .builder()
                .name(brandRequest.getName())
                .thumbnail(brandRequest.getThumbnail())
                .build();
        brandRepository.save(newBrand);
    }

    public Brand getBrandById(String id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
    }

    public List<Brand> getAllBrands(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Brand> brandPage = brandRepository.findAll(pageRequest);
        return brandPage.getContent();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateBrand(String brandId, BrandRequest brandDTO) {
        Brand existingBrand = getBrandById(brandId);
        existingBrand.setName(brandDTO.getName());
        existingBrand.setThumbnail(brandDTO.getThumbnail());
        brandRepository.save(existingBrand);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBrand(String id) {
        brandRepository.deleteById(id);
    }


}
