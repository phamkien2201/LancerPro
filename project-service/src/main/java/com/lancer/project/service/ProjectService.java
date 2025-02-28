package com.lancer.project.service;

import com.lancer.project.dto.ApiResponse;
import com.lancer.project.dto.request.ProjectRequest;
import com.lancer.project.dto.response.ProjectResponse;
import com.lancer.project.dto.response.ProjectListResponse;
import com.lancer.project.dto.response.UserResponse;
import com.lancer.project.entity.Category;
import com.lancer.project.entity.Project;
import com.lancer.project.entity.ProjectIndex;
import com.lancer.project.entity.ProjectStatus;
import com.lancer.project.exception.AppException;
import com.lancer.project.exception.ErrorCode;
import com.lancer.project.mapper.ProjectMapper;
import com.lancer.project.repository.CategoryRepository;
import com.lancer.project.repository.ProjectRepository;
import com.lancer.project.repository.ProjectSearchRepository;
import com.lancer.project.repository.httpclient.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProjectService {

    ProjectMapper projectMapper;
    ProjectRepository projectRepository;
    CategoryRepository categoryRepository;
    UserClient userClient;
    ProjectSearchRepository projectSearchRepository;

    public ProjectResponse createProject(ProjectRequest request){
        ApiResponse<UserResponse> user = userClient.getUser(request.getClientId());
        if (user == null || user.getResult() == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        Project project = projectMapper.toProject(request);
        project.setStatus(ProjectStatus.OPEN);
        project = projectRepository.save(project);

        ProjectIndex projectIndex = new ProjectIndex(
                project.getId(), project.getTitle(), project.getDescription(),
                project.getCategoryId(), project.getBudgetMin(), project.getBudgetMax(),
                project.getSkills(), project.getAddress(), project.getStatus(), project.getDeadline()
        );
        projectSearchRepository.save(projectIndex);
        return projectMapper.toProjectResponse(project);
    }

    public Page<ProjectResponse> getAllProjects(PageRequest pageRequest) {
        return projectRepository
                .findAll(pageRequest)
                .map(projectMapper::toProjectResponse);
    }

    public ProjectResponse getProjectById(String id) {
       Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cannot found project with id "+id));
        return projectMapper.toProjectResponse(project);
    }

    public Page<ProjectResponse> getProjectsByClientId(String clientId,PageRequest pageRequest) {
        return projectRepository.findProjectByClientId(clientId, pageRequest)
                .map(projectMapper::toProjectResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse updateProject(String projectId, ProjectRequest request) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));

        projectMapper.updateProject(project, request);
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProject(String id) {
        projectRepository.findById(id).ifPresent(projectRepository::delete);
    }

    public void updateProjectStatus(String projectId, String status) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found!"));
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid project status: " + status);
        }
        project.setStatus(status);
        projectRepository.save(project);
    }

    private boolean isValidStatus(String status) {
        return ProjectStatus.OPEN.equals(status) ||
                ProjectStatus.IN_PROGRESS.equals(status) ||
                ProjectStatus.CLOSED.equals(status);
    }

    public ProjectListResponse findProjectsByCategoryId(String categoryId, int page, int limit, String sortBy, String sortDirection) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("No category found with id: " + categoryId);
        }

        List<Category> subCategories = categoryRepository.findByParentId(categoryId);
        Page<Project> projects;
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sortBy));

        if (subCategories.isEmpty()) {
            projects = projectRepository.findByCategoryId(categoryId, pageable);
        } else {
            List<String> categoryIds = findAllSubCategoryIds(categoryId);
            projects = projectRepository.findByCategoryIdIn(categoryIds, pageable);
        }

        List<ProjectResponse> productResponses = projects.map(projectMapper::toProjectResponse).getContent();
        int totalPages = projects.getTotalPages();

        return ProjectListResponse.builder()
                .projects(productResponses)
                .totalPages(totalPages)
                .build();
    }

    private List<String> findAllSubCategoryIds(String parentId) {
        List<String> categoryIds = new ArrayList<>();
        categoryIds.add(parentId);
        List<Category> subCategories = categoryRepository.findByParentId(parentId);
        for (Category subCategory : subCategories) {
            categoryIds.add(subCategory.getId());
            categoryIds.addAll(findAllSubCategoryIds(subCategory.getId()));
        }
        return categoryIds;
    }

}
