package com.cosmetics.order.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class MapboxGeocodingResponse {
    private List<Feature> features;

    @Data
    public static class Feature {
        private String place_name;
        private List<Double> center; // [longitude, latitude]
    }
}