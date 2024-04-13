package com.ggmaciel.orlaapi.domain.project;

import com.ggmaciel.orlaapi.domain.project.dto.CreateProjectDTO;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.PROJECT_ALREADY_EXISTS;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project create(CreateProjectDTO createProjectDTO) {
        String name = createProjectDTO.getName();
        if (projectRepository.findByName(name) != null) {
            throw new EntityAlreadyExistsException(PROJECT_ALREADY_EXISTS);
        }
        Project project = new Project(name);
        return projectRepository.save(project);
    }

    public List<Project> getAllProjectsWithEmployees() {
        return Collections.emptyList();
    }
}
