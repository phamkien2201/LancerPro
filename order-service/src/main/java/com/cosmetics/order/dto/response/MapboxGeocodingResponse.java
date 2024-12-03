package com.cosmetics.order.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class MapboxGeocodingResponse {
    private List<Feature> features;

    @Data
    public static class Feature {
        private List<Double> center; // Center chứa tọa độ [longitude, latitude]
    }
}