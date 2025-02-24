package com.lancer.project.service;

import com.lancer.project.dto.ApiResponse;
import com.lancer.project.dto.request.ProjectRequest;
import com.lancer.project.dto.response.ProjectResponse;
import com.lancer.project.dto.response.UserResponse;
import com.lancer.project.entity.Project;
import com.lancer.project.exception.AppException;
import com.lancer.project.exception.ErrorCode;
import com.lancer.project.mapper.ProjectMapper;
import com.lancer.project.repository.ProjectRepository;
import com.lancer.project.repository.httpclient.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProjectService {

    ProjectMapper projectMapper;
    ProjectRepository projectRepository;
    UserClient userClient;

    public ProjectResponse createProject(ProjectRequest request){
        ApiResponse<UserResponse> user = userClient.getUser(request.getClientId());
        if (user == null || user.getResult() == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        Project project = projectMapper.toProject(request);
        project = projectRepository.save(project);
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
    public Page<ProjectResponse> getProjectsByCategory(String category,PageRequest pageRequest) {
        return projectRepository.findByCategory(category, pageRequest)
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



}
