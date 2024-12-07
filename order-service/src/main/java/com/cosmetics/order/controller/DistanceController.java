package com.cosmetics.order.controller;

import com.cosmetics.order.dto.ApiResponse;
import com.cosmetics.order.dto.request.DistanceRequest;
import com.cosmetics.order.service.DistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/distance")
public class DistanceController {

    private final DistanceService distanceService;

    @GetMapping
    public ApiResponse<Double> getDistance(
            @RequestParam String originAddress,
            @RequestParam String destinationAddress) {
        try {
            // Kiểm tra địa chỉ không được để trống
            if (originAddress == null || originAddress.isEmpty()) {
                return ApiResponse.<Double>builder()
                        .code(4000)
                        .message("Địa chỉ xuất phát không được để trống")
                        .build();
            }

            if (destinationAddress == null || destinationAddress.isEmpty()) {
                return ApiResponse.<Double>builder()
                        .code(4000)
                        .message("Địa chỉ đích không được để trống")
                        .build();
            }

            // Lấy tọa độ từ các địa chỉ
            String originCoordinates = distanceService.getCoordinatesFromAddress(originAddress);
            String destinationCoordinates = distanceService.getCoordinatesFromAddress(destinationAddress);

            // Tính khoảng cách
            double distance = distanceService.calculateDistance(originCoordinates, destinationCoordinates);

            // Trả về kết quả thành công
            return ApiResponse.<Double>builder()
                    .code(1000)
                    .message("Tính khoảng cách thành công")
                    .result(distance)
                    .build();

        } catch (RuntimeException e) {
            // Xử lý các ngoại lệ từ service
            return ApiResponse.<Double>builder()
                    .code(5000)
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            // Xử lý các ngoại lệ không mong muốn
            return ApiResponse.<Double>builder()
                    .code(9999)
                    .message("Có lỗi không xác định xảy ra")
                    .build();
        }
    }
}