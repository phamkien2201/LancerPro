package com.lancer.profile.controller;

import com.lancer.profile.dto.ApiResponse;
import com.lancer.profile.dto.request.PortfolioRequest;
import com.lancer.profile.dto.request.PortfolioUpdateRequest;
import com.lancer.profile.dto.request.WorkProfileRequest;
import com.lancer.profile.dto.request.WorkProfileUpdateRequest;
import com.lancer.profile.dto.response.PortfolioResponse;
import com.lancer.profile.dto.response.WorkProfileResponse;
import com.lancer.profile.entity.Portfolio;
import com.lancer.profile.entity.WorkProfile;
import com.lancer.profile.service.PortfolioService;
import com.lancer.profile.service.WorkProfileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/profiles")
public class ProfileController {

    PortfolioService portfolioService;
    WorkProfileService workProfileService;

    // Portfolio Endpoints
    @PostMapping("/portfolio")
    ApiResponse<PortfolioResponse> createPortfolio(@RequestBody PortfolioRequest request) {
        return ApiResponse.<PortfolioResponse>builder()
                .result(portfolioService.createPortfolio(request))
                .build();
    }

    @GetMapping("/portfolio/{userId}")
    ApiResponse<PortfolioResponse> getPortfolio(@PathVariable String userId) {
        return ApiResponse.<PortfolioResponse>builder()
                .result(portfolioService.getPortfolio(userId))
                .build();
    }

    @GetMapping("/portfolios")
    ApiResponse<List<PortfolioResponse>> getAllPortfolios() {
        return ApiResponse.<List<PortfolioResponse>>builder()
                .result(portfolioService.getAllPortfolio())
                .build();
    }

    @PutMapping("/portfolio/{userId}")
    ApiResponse<PortfolioResponse> updatePortfolio(@PathVariable String userId, @RequestBody PortfolioUpdateRequest request) {
        return ApiResponse.<PortfolioResponse>builder()
                .result(portfolioService.updatePortfolio(userId, request))
                .build();
    }
    @DeleteMapping("/portfolio/{id}")
    public void deletePortfolioById(
            @PathVariable String id) {
        portfolioService.deletePortfolio(id);
    }


    // Work Profile Endpoints
    @PostMapping("/work-profile")
    ApiResponse<WorkProfileResponse> createWorkProfile(@RequestBody WorkProfileRequest request) {
        return ApiResponse.<WorkProfileResponse>builder()
                .result(workProfileService.createWorkProfile(request))
                .build();
    }

    @GetMapping("/work-profile/{userId}")
    ApiResponse<WorkProfileResponse> getWorkProfile(@PathVariable String userId) {
        return ApiResponse.<WorkProfileResponse>builder()
                .result(workProfileService.getWorkProfile(userId))
                .build();
    }

    @GetMapping("/work-profiles")
    ApiResponse<List<WorkProfileResponse>> getAllWorkProfiles() {
        return ApiResponse.<List<WorkProfileResponse>>builder()
                .result(workProfileService.getAllWorkProfiles())
                .build();
    }

    @PutMapping("/work-profile/{userId}")
    ApiResponse<WorkProfileResponse> updateWorkProfile(@PathVariable String userId, @RequestBody WorkProfileUpdateRequest request) {
        return ApiResponse.<WorkProfileResponse>builder()
                .result(workProfileService.updateWorkProfile(userId, request))
                .build();
    }

    @DeleteMapping("/work-profile/{id}")
    public void deleteWorkProfileById(
            @PathVariable String id) {
        workProfileService.deleteWorkProfile(id);
    }
}
