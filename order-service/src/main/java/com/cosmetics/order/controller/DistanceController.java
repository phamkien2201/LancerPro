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
            @RequestParam String origin,
            @RequestParam String destination) {
        double distance = distanceService.calculateDistance(origin, destination);
        return ApiResponse.<Double>builder()
                .code(1000)
                .result(distance)
                .build();
    }

//    @GetMapping("/by-address")
//    public ApiResponse<Double> getDistanceByAddress(
//            @RequestParam String originAddress,
//            @RequestParam String destinationAddress) {
//        String originCoordinates = distanceService.convertAddressToCoordinates(originAddress);
//        String destinationCoordinates = distanceService.convertAddressToCoordinates(destinationAddress);
//
//        double distance = distanceService.calculateDistance(originCoordinates, destinationCoordinates);
//
//        return ApiResponse.<Double>builder()
//                .code(1000)
//                .result(distance)
//                .build();
//    }
}