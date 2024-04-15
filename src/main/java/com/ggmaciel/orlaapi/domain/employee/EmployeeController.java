package com.ggmaciel.orlaapi.domain.employee;

import com.ggmaciel.orlaapi.domain.employee.dto.AddProjectDTO;
import com.ggmaciel.orlaapi.domain.employee.dto.CreateEmployeeDTO;
import com.ggmaciel.orlaapi.domain.employee.dto.EmployeeProjectDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody CreateEmployeeDTO createEmployeeDTO) {
        return ResponseEntity.ok(employeeService.create(createEmployeeDTO));
    }

    @PostMapping("/add-project")
    public ResponseEntity<Map<String, String>> addProject(@Valid @RequestBody AddProjectDTO addProjectDTO) {
        employeeService.addProject(addProjectDTO);
        return ResponseEntity.ok(Map.of("message", "Project added to employee"));
    }

    @GetMapping("/{employeeId}/projects")
    public ResponseEntity<Set<EmployeeProjectDTO>> findProjectsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.findProjectsByEmployeeId(employeeId));
    }
}
