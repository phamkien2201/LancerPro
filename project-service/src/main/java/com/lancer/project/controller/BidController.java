package com.lancer.project.controller;

import com.lancer.project.dto.ApiResponse;
import com.lancer.project.dto.request.BidRequest;
import com.lancer.project.dto.response.BidResponse;
import com.lancer.project.dto.response.ProjectResponse;
import com.lancer.project.service.BidService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/bids")
public class BidController {

    BidService bidService;

    @PostMapping("/create")
    @Operation(summary = "Tạo đấu thầu")
    public ApiResponse<BidResponse> createBid(@RequestBody @Valid BidRequest request) {
        return ApiResponse.<BidResponse>builder()
                .result(bidService.createBid(request))
                .build();
    }

    @GetMapping("/get-all")
    @Operation(summary = "Lấy danh sách đấu thầu")
    public ApiResponse<Page<BidResponse>> getAllBids(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 20;
        }
        PageRequest pageRequest = PageRequest.of(page, limit);
        return ApiResponse.<Page<BidResponse>>builder()
                .result(bidService.getAllBids(pageRequest))
                .build();
    }

    @GetMapping("/{bidId}")
    @Operation(summary = "Lấy đấu thầu theo ID")
    public ApiResponse<BidResponse> getBidById(@PathVariable String bidId) {
        return ApiResponse.<BidResponse>builder()
                .result(bidService.getBidById(bidId))
                .build();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Lấy danh sách đấu thầu theo projectId")
    public ApiResponse<Page<BidResponse>> getBidsByProjectId(
            @PathVariable String projectId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 20;
        }
        PageRequest pageRequest = PageRequest.of(page, limit);
        return ApiResponse.<Page<BidResponse>>builder()
                .result(bidService.getBidsByProjectId(projectId, pageRequest))
                .build();
    }

    @GetMapping("/freelancer/{freelancerId}")
    @Operation(summary = "Lấy danh sách đấu thầu theo freelancerId")
    public ApiResponse<Page<BidResponse>> getBidsByFreelancerId(
            @RequestParam(value = "freelancerId") String freelancerId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 20;
        }
        PageRequest pageRequest = PageRequest.of(page, limit);
        return ApiResponse.<Page<BidResponse>>builder()
                .result(bidService.getBidsByFreelancerId(freelancerId, pageRequest))
                .build();
    }

    @PutMapping("/update/{bidId}")
    @Operation(summary = "Cập nhật đấu thầu")
    public ApiResponse<BidResponse> updateBid(
            @PathVariable String bidId,
            @RequestBody @Valid BidRequest request) {
        return ApiResponse.<BidResponse>builder()
                .result(bidService.updateBid(bidId, request))
                .build();
    }

    @PutMapping("/{bidId}/status")
    @Operation(summary = "Cập nhật trạng thái đấu thầu")
    public ApiResponse<Void> updateBidStatus(
            @PathVariable String bidId,
            @RequestParam("status") String status) {
        bidService.updateBidStatus(bidId, status);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/delete/{bidId}")
    @Operation(summary = "Xóa đấu thầu")
    public void deleteBid(@PathVariable String bidId) {
        bidService.deleteBid(bidId);
    }
}
