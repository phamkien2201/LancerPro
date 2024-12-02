package com.cosmetics.order.repository.httpclient;

import com.cosmetics.order.dto.request.ProductRequest;
import com.cosmetics.order.dto.ApiResponse;
import com.cosmetics.order.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", url = "${app.services.product}")
public interface ProductClient {

    @GetMapping(value = "/{productId}")
    ApiResponse<ProductResponse> getProductById(@PathVariable("productId") String productId);
}
