package com.ggmaciel.orlaapi.domain.employee;

import com.ggmaciel.orlaapi.domain.employee.dto.AddProjectDTO;
import com.ggmaciel.orlaapi.domain.employee.dto.CreateEmployeeDTO;
import com.ggmaciel.orlaapi.domain.project.Project;
import com.ggmaciel.orlaapi.domain.project.ProjectService;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import com.ggmaciel.orlaapi.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.ENTITY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private ProjectService projectService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void shouldCreateAEmployee() {
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO("Fulano", "12345678900", "", 300L);

        assertDoesNotThrow(() -> employeeService.create(createEmployeeDTO));

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAnEmployeeWithAnCpfThatWasAlreadyUsed() {
        String cpf = "12345678900";
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO("Fulano", cpf, "", 300L);

        when(employeeRepository.findByCpf(cpf)).thenReturn(new Employee("Fulano", cpf, "", 300L));

        assertThrows(EntityAlreadyExistsException.class, () -> employeeService.create(createEmployeeDTO));

        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAnEmployeeWithAnEmailThatWasAlreadyUsed() {
        String email = "mail@mail.com";
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO("Fulano", "12345678900", email, 300L);

        when(employeeRepository.findByEmail(email)).thenReturn(new Employee("Fulano", "12345678900", email, 300L));

        assertThrows(EntityAlreadyExistsException.class, () -> employeeService.create(createEmployeeDTO));

        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void shouldAddProjectToEmployeeWhenEmployeeAndProjectExist() {
        Long employeeId = 1L;
        Long projectId = 1L;
        AddProjectDTO addProjectDTO = new AddProjectDTO(employeeId, projectId);
        Employee employee = new Employee("Fulano", "12345678900", "mail@mail.com", 300L);
        Project project = new Project("Project1");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(projectService.findById(projectId)).thenReturn(project);

        employeeService.addProject(addProjectDTO);

        verify(employeeRepository, times(1)).save(employee);
        assertTrue(employee.getProjects().contains(project));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenEmployeeDoesNotExist() {
        Long employeeId = 1L;
        Long projectId = 1L;
        AddProjectDTO addProjectDTO = new AddProjectDTO(employeeId, projectId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> employeeService.addProject(addProjectDTO));
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenProjectDoesNotExist() {
        Long employeeId = 1L;
        Long projectId = 1L;
        AddProjectDTO addProjectDTO = new AddProjectDTO(employeeId, projectId);
        Employee employee = new Employee("Fulano", "12345678900", "mail@mail.com", 300L);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(projectService.findById(projectId)).thenThrow(new EntityNotFoundException(ENTITY_NOT_FOUND));

        assertThrows(EntityNotFoundException.class, () -> employeeService.addProject(addProjectDTO));
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }
}