package com.ggmaciel.orlaapi.domain.employee;

import com.ggmaciel.orlaapi.domain.employee.dto.AddProjectDTO;
import com.ggmaciel.orlaapi.domain.employee.dto.CreateEmployeeDTO;
import com.ggmaciel.orlaapi.domain.employee.dto.EmployeeProjectDTO;
import com.ggmaciel.orlaapi.domain.project.Project;
import com.ggmaciel.orlaapi.domain.project.ProjectService;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import com.ggmaciel.orlaapi.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.*;
import static java.util.stream.Collectors.toSet;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ProjectService projectService;

    public EmployeeService(EmployeeRepository employeeRepository, ProjectService projectService) {
        this.employeeRepository = employeeRepository;
        this.projectService = projectService;
    }

    public Employee create(CreateEmployeeDTO createEmployeeDTO) {
        String cpf = createEmployeeDTO.getCpf();
        String email = createEmployeeDTO.getEmail();

        checkIfExists(cpf, email);

        Employee employee = new Employee(cpf, email, createEmployeeDTO.getName(), createEmployeeDTO.getSalary());

        return employeeRepository.save(employee);
    }

    @Transactional
    public void addProject(AddProjectDTO addProjectDTO) {
        Employee employee = employeeRepository.findById(addProjectDTO.getEmployeeId()).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND));
        Project project = projectService.findById(addProjectDTO.getProjectId());

        employee.getProjects().add(project);
        employeeRepository.save(employee);
    }

    public Set<EmployeeProjectDTO> findProjectsByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND));
        return employee.getProjects().stream().map(project -> new EmployeeProjectDTO(project.getId(), project.getName(), project.getDateOfCreation())).collect(toSet());
    }

    private void checkIfExists(String cpf, String email) {
        if (employeeRepository.findByCpf(cpf) != null) {
            throw new EntityAlreadyExistsException(EMPLOYEE_WITH_CPF_ALREADY_EXISTS);
        }

        if (employeeRepository.findByEmail(email) != null) {
            throw new EntityAlreadyExistsException(EMPLOYEE_WITH_EMAIL_ALREADY_EXISTS);
        }
    }
}
