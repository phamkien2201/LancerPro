package com.lancer.project.controller;

import com.lancer.project.dto.ApiResponse;
import com.lancer.project.dto.request.ProjectRequest;
import com.lancer.project.dto.response.ProjectListResponse;
import com.lancer.project.dto.response.ProjectResponse;
import com.lancer.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectController {

    ProjectService projectService;

    @PostMapping("/create")
    @Operation(summary = "Tạo dự án")
    public ApiResponse<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest request) {
        return ApiResponse.<ProjectResponse>builder()
                .result(projectService.createProject(request))
                .build();
    }

    @GetMapping("/get-all")
    @Operation(summary = "Lấy danh sách dự án")
    public ApiResponse<ProjectListResponse> getAllProjects(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 20;
        }
        if (sortBy == null) {
            sortBy = "createdAt";
        }
        if (sortDirection == null) {
            sortDirection = "desc";
        }

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(direction, sortBy));
        Page<ProjectResponse> projectResponses = projectService.getAllProjects(pageRequest);
        int totalPages = projectResponses.getTotalPages();
        List<ProjectResponse> projects = projectResponses.getContent();
        return ApiResponse.<ProjectListResponse>builder()
                .result(ProjectListResponse.builder()
                        .projects(projects)
                        .totalPages(totalPages)
                        .build())
                .build();
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Lấy danh sách project theo danh mục")
    public ApiResponse<ProjectListResponse> findProjectsByCategoryId(
            @PathVariable String categoryId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit",required = false) Integer limit,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 20;
        }
        if (sortBy == null) {
            sortBy = "createdAt";
        }
        if (sortDirection == null) {
            sortDirection = "desc";
        }

        ProjectListResponse projectListResponse = projectService.findProjectsByCategoryId(categoryId, page, limit, sortBy, sortDirection);
        return ApiResponse.<ProjectListResponse>builder()
                .result(projectListResponse)
                .build();
    }


    @GetMapping("/client/{clientId}")
    @Operation(summary = "Lấy danh sách dự án theo clientId")
    public ApiResponse<ProjectListResponse> getProjectsByClientId(
            @PathVariable String clientId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 20;
        }
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<ProjectResponse> projectResponses = projectService.getProjectsByClientId(clientId, pageRequest);
        int totalPages = projectResponses.getTotalPages();
        List<ProjectResponse> projects = projectResponses.getContent();
        return ApiResponse.<ProjectListResponse>builder()
                .result(ProjectListResponse.builder()
                        .projects(projects)
                        .totalPages(totalPages)
                        .build())
                .build();
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Lấy dự án theo ID")
    public ApiResponse<ProjectResponse> getProjectById(@PathVariable String projectId) {
        return ApiResponse.<ProjectResponse>builder()
                .result(projectService.getProjectById(projectId))
                .build();
    }


    @PutMapping("/update/{projectId}")
    @Operation(summary = "Cập nhật dự án")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProjectResponse> updateProject(
            @PathVariable String projectId,
            @RequestBody @Valid ProjectRequest request) {
        return ApiResponse.<ProjectResponse>builder()
                .result(projectService.updateProject(projectId, request))
                .build();
    }

    @PutMapping("/{projectId}/status")
    @Operation(summary = "Cập nhật trạng thái dự án")
    public ApiResponse<Void> updateProjectStatus(
            @PathVariable String projectId,
            @RequestParam("status") String status) {
        projectService.updateProjectStatus(projectId, status);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/delete/{projectId}")
    @Operation(summary = "Xóa dự án")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProject(@PathVariable String projectId) {
        projectService.deleteProject(projectId);
    }
}
