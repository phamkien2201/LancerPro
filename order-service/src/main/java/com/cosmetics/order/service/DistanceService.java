package com.cosmetics.order.service;

import com.cosmetics.order.dto.response.MapboxGeocodingResponse;
import com.cosmetics.order.dto.response.MapboxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class DistanceService {

    @Value("${app.mapbox.access-token}")
    String mapboxAccessToken;

    RestTemplate restTemplate;
    public DistanceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String shortenAddress(String address) {
        String[] parts = address.split(",");
        if (parts.length > 3) {
            return parts[parts.length - 2].trim() + ", " + parts[parts.length - 1].trim();
        }
        return address;
    }

    public String getCoordinatesFromAddress(String address) {
        try {
            String shortenedAddress = shortenAddress(address);
            String encodedAddress = URLEncoder.encode(shortenedAddress, StandardCharsets.UTF_8.toString());

            URI url = UriComponentsBuilder.fromHttpUrl("https://api.mapbox.com/geocoding/v5/mapbox.places/" + encodedAddress + ".json")
                    .queryParam("access_token", mapboxAccessToken)
                    .queryParam("limit", 1)
                    .build().toUri();

            log.info("Calling Mapbox Geocoding API with shortened address: {}", shortenedAddress);

            // Gọi API
            MapboxGeocodingResponse response = restTemplate.getForObject(url, MapboxGeocodingResponse.class);

            if (response != null && !response.getFeatures().isEmpty()) {
                List<Double> coordinates = response.getFeatures().get(0).getCenter();
                log.info("Coordinates for address: {} are {}, {}", shortenedAddress, coordinates.get(0), coordinates.get(1));
                return coordinates.get(0) + "," + coordinates.get(1); // longitude, latitude
            }

            log.error("Mapbox Geocoding response is empty or invalid for address: {}", shortenedAddress);
            throw new RuntimeException("Không thể tìm thấy tọa độ.");
        } catch (Exception e) {
            log.error("Error calling Mapbox Geocoding API: ", e);
            throw new RuntimeException("Lỗi khi lấy tọa độ từ địa chỉ: " + e.getMessage());
        }
    }

    public double calculateDistance(String origin, String destination) {
        try {
            // Build URL with query parameters
            URI url = UriComponentsBuilder.fromHttpUrl("https://api.mapbox.com/directions/v5/mapbox/driving/" + origin + ";" + destination)
                    .queryParam("geometries", "geojson")
                    .queryParam("access_token", mapboxAccessToken)
                    .build().toUri();

            log.info("Calling Mapbox Directions API with URL: {}", url);

            // Use GET request to calculate distance
            MapboxResponse response = restTemplate.getForObject(url, MapboxResponse.class);

            if (response != null) {
                if (response.getRoutes().isEmpty()) {
                    log.error("Mapbox Directions API returned empty routes for coordinates: {}", origin + ";" + destination);
                    throw new RuntimeException("Không thể tính khoảng cách.");
                }
                double distanceInKm = response.getRoutes().get(0).getDistance() / 1000.0; // Convert meters to km
                log.info("Distance calculated: {} km", distanceInKm);
                return distanceInKm;
            }

            log.error("Mapbox Directions API response is empty for coordinates: {}", origin + ";" + destination);
            throw new RuntimeException("Không thể tính khoảng cách.");
        } catch (Exception e) {
            log.error("Error calling Mapbox Directions API: ", e);
            return 4.0;
        }
    }
}