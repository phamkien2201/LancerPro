package com.lancer.project.repository;

import com.lancer.project.entity.ProjectIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface ProjectSearchRepository extends ElasticsearchRepository<ProjectIndex, String> {
    List<ProjectIndex> findByTitleContainingIgnoreCase(String title);
    List<ProjectIndex> findBySkillsIn(List<String> skills);
}
