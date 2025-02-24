package com.lancer.project.repository;

import com.lancer.project.dto.response.ProjectResponse;
import com.lancer.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
    ProjectResponse findProjectById(String id);
    Page<Project> findProjectByClientId(String id, PageRequest pageRequest);
    Page<Project> findByCategory(String category, PageRequest pageRequest);
}
