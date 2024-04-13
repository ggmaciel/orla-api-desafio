package com.ggmaciel.orlaapi.domain.project;

import com.ggmaciel.orlaapi.domain.project.dto.CreateProjectDTO;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Test
    void shouldCreateAProject() {
        CreateProjectDTO createProjectDTO = new CreateProjectDTO();
        createProjectDTO.setName("Project 01");

        assertDoesNotThrow(() -> projectService.create(createProjectDTO));

        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAProjectThatAlreadyExists() {
        String name = "Project 01";
        CreateProjectDTO createProjectDTO = new CreateProjectDTO();
        createProjectDTO.setName(name);

        when(projectRepository.findByName(name)).thenReturn(new Project(name));

        assertThrows(EntityAlreadyExistsException.class, () -> projectService.create(createProjectDTO));

        verify(projectRepository, times(0)).save(any(Project.class));
    }
}