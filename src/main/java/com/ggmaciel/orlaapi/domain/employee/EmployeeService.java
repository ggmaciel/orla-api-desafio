package com.ggmaciel.orlaapi.domain.employee;

import com.ggmaciel.orlaapi.domain.employee.dto.CreateEmployeeDTO;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import org.springframework.stereotype.Service;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.EMPLOYEE_WITH_CPF_ALREADY_EXISTS;
import static com.ggmaciel.orlaapi.helpers.ConstantHelper.EMPLOYEE_WITH_EMAIL_ALREADY_EXISTS;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee create(CreateEmployeeDTO createEmployeeDTO) {
        String cpf = createEmployeeDTO.getCpf();
        String email = createEmployeeDTO.getEmail();

        checkIfExists(cpf, email);

        Employee employee = new Employee(cpf, email, createEmployeeDTO.getName(), createEmployeeDTO.getSalary());

        return employeeRepository.save(employee);
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
