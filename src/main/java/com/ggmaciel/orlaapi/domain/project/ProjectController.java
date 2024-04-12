package com.ggmaciel.orlaapi.domain.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/with-employees")
    public ResponseEntity<List<Project>> getAllProjectsWithEmployees() {
        return ResponseEntity.ok(projectService.getAllProjectsWithEmployees());
    }
}
