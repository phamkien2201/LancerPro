package com.cosmetics.order.service;

import com.cosmetics.order.dto.response.MapboxGeocodingResponse;
import com.cosmetics.order.dto.response.MapboxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

//    public String convertAddressToCoordinates(String address) {
//        try {
//            String baseUrl = "https://api.mapbox.com/geocoding/v5/mapbox.places/";
//            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
//            String url = String.format(
//                    "%s%s.json?access_token=%s",
//                    baseUrl,
//                    encodedAddress,
//                    mapboxAccessToken
//            );
//
//            MapboxGeocodingResponse response = restTemplate.getForObject(url, MapboxGeocodingResponse.class);
//
//            if (response != null && !response.getFeatures().isEmpty()) {
//                List<Double> coordinates = response.getFeatures().get(0).getCenter();
//                return coordinates.get(0) + "," + coordinates.get(1); // Dạng "longitude,latitude"
//            }
//
//            throw new RuntimeException("Không thể chuyển đổi địa chỉ thành tọa độ.");
//        } catch (Exception e) {
//            log.error("Lỗi khi gọi Mapbox Geocoding API: ", e);
//            throw new RuntimeException("Lỗi khi chuyển đổi địa chỉ thành tọa độ.");
//        }
//    }


    public double calculateDistance(String origin, String destination) {
        try {
            String baseUrl = "https://api.mapbox.com/directions/v5/mapbox/driving/";
            String coordinates = origin + ";" + destination;
            String url = String.format(
                    "%s%s?geometries=geojson&access_token=%s",
                    baseUrl,
                    coordinates,
                    mapboxAccessToken
            );

            MapboxResponse response = restTemplate.getForObject(url, MapboxResponse.class);

            if (response != null && !response.getRoutes().isEmpty()) {
                return response.getRoutes().get(0).getDistance() / 1000.0; // Mét sang km
            }

            throw new RuntimeException("Không thể tính khoảng cách.");
        } catch (Exception e) {
            log.error("Lỗi khi gọi Mapbox API: ", e);
            throw new RuntimeException("Lỗi khi tính khoảng cách.");
        }
    }

}