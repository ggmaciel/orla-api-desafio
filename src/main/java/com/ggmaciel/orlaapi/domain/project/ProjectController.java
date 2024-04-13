package com.ggmaciel.orlaapi.domain.project;

import com.ggmaciel.orlaapi.domain.project.dto.CreateProjectDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> create(@Valid @RequestBody CreateProjectDTO createProjectDTO) {
        return ResponseEntity.ok(projectService.create(createProjectDTO));
    }

    @GetMapping("/with-employees")
    public ResponseEntity<List<Project>> findProjectsWithRespectiveEmployees() {
        return ResponseEntity.ok(projectService.findProjectsWithRespectiveEmployees());
    }
}
