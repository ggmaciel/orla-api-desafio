package com.ggmaciel.orlaapi.domain.project;

import com.ggmaciel.orlaapi.domain.employee.Employee;
import com.ggmaciel.orlaapi.domain.project.dto.CreateProjectDTO;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import com.ggmaciel.orlaapi.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.ENTITY_NOT_FOUND;
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

    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND));
    }

    public List<Project> findProjectsWithRespectiveEmployees() {
        return projectRepository.findAll();
    }

    public Set<Employee> findEmployeesByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND));
        return project.getEmployees();
    }
}
