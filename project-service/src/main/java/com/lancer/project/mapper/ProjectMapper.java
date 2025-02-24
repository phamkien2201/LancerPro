package com.lancer.project.mapper;

import com.lancer.project.dto.request.ProjectRequest;
import com.lancer.project.dto.response.ProjectResponse;
import com.lancer.project.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toProject(ProjectRequest request);
    ProjectResponse toProjectResponse(Project entity);

    @Mapping(target = "id", ignore = true)
    void updateProject(@MappingTarget Project project, ProjectRequest request);
}
