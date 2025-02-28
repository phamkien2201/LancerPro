package com.lancer.bid.repository.httpclient;

import com.lancer.bid.dto.ApiResponse;
import com.lancer.bid.dto.response.ProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service", url = "${app.services.projects}")
public interface ProjectClient {
    @GetMapping("/{projectId}")
    @Operation(summary = "Lấy dự án theo ID")
    ApiResponse<ProjectResponse> getProjectById(@PathVariable("projectId") String projectId);


}

