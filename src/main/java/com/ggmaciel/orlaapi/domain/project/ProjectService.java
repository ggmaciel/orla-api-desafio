package com.ggmaciel.orlaapi.domain.project;

import com.ggmaciel.orlaapi.domain.project.dto.CreateProjectDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project create(CreateProjectDTO createProjectDTO) {
        Project project = new Project(createProjectDTO.getName());
        return projectRepository.save(project);
    }

    public List<Project> getAllProjectsWithEmployees() {
        return Collections.emptyList();
    }
}
