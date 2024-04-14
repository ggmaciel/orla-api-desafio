package com.ggmaciel.orlaapi.domain.project;

import com.ggmaciel.orlaapi.domain.employee.Employee;
import com.ggmaciel.orlaapi.domain.project.dto.CreateProjectDTO;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import com.ggmaciel.orlaapi.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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
        CreateProjectDTO createProjectDTO = new CreateProjectDTO("Project 01");

        assertDoesNotThrow(() -> projectService.create(createProjectDTO));

        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAProjectThatAlreadyExists() {
        String name = "Project 01";
        CreateProjectDTO createProjectDTO = new CreateProjectDTO(name);

        when(projectRepository.findByName(name)).thenReturn(new Project(name));

        assertThrows(EntityAlreadyExistsException.class, () -> projectService.create(createProjectDTO));

        verify(projectRepository, times(0)).save(any(Project.class));
    }

    @Test
    void shouldThrowAnExceptionWhenFindingAProjectThatDoesNotExist() {
        Long id = 1L;

        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.findById(id));
    }

    @Test
    void shouldReturnListOfProjectsWithRespectiveEmployees() {
        Project project = new Project("Project 01");
        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<Project> projects = projectService.findProjectsWithRespectiveEmployees();

        assertFalse(projects.isEmpty());
        assertEquals(1, projects.size());
        assertEquals(project, projects.getFirst());
    }

    @Test
    void shouldReturnEmptyListWhenNoProjectsWithRespectiveEmployeesExist() {
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        List<Project> projects = projectService.findProjectsWithRespectiveEmployees();

        assertTrue(projects.isEmpty());
    }

    @Test
    void shouldReturnSetOfEmployeesWhenFindingEmployeesByProjectId() {
        Long projectId = 1L;
        Project project = new Project("Project 01");
        project.setId(projectId);
        Employee employee = new Employee();
        project.setEmployees(new HashSet<>(Set.of(employee)));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Set<Employee> employees = projectService.findEmployeesByProjectId(projectId);

        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());
        assertTrue(employees.contains(employee));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenFindingEmployeesByNonExistentProjectId() {
        Long projectId = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.findEmployeesByProjectId(projectId));
    }

    @Test
    void shouldReturnEmptySetWhenProjectHasNoEmployees() {
        Long projectId = 1L;
        Project project = new Project("Project 01");
        project.setId(projectId);
        project.setEmployees(new HashSet<>());
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Set<Employee> employees = projectService.findEmployeesByProjectId(projectId);

        assertTrue(employees.isEmpty());
    }
}