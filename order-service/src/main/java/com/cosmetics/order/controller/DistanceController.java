package com.cosmetics.order.controller;

import com.cosmetics.order.dto.ApiResponse;
import com.cosmetics.order.service.DistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/distance")
public class DistanceController {

    private final DistanceService distanceService;

    @GetMapping
    public ApiResponse<Double> getDistance(
            @RequestParam String originAddress,
            @RequestParam String destinationAddress) {
        String originCoordinates = distanceService.getCoordinatesFromAddress(originAddress);
        String destinationCoordinates = distanceService.getCoordinatesFromAddress(destinationAddress);
        double distance = distanceService.calculateDistance(originCoordinates, destinationCoordinates);
        return ApiResponse.<Double>builder()
                .code(1000)
                .result(distance)
                .build();
    }


}