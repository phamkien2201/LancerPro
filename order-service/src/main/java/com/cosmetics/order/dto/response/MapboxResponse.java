package com.cosmetics.order.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class MapboxResponse {
    private List<Route> routes;

    @Data
    public static class Route {
        private double distance;
        private double duration;
    }

}
