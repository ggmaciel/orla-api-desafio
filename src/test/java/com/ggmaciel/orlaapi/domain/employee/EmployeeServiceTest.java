package com.ggmaciel.orlaapi.domain.employee;

import com.ggmaciel.orlaapi.domain.employee.dto.CreateEmployeeDTO;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void shouldCreateAEmployee() {
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO("John Doe", "12345678900", "", 300L);

        assertDoesNotThrow(() -> employeeService.create(createEmployeeDTO));

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAnEmployeeWithAnCpfThatWasAlreadyUsed() {
        String cpf = "12345678900";
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO("John Doe", cpf, "", 300L);

        when(employeeRepository.findByCpf(cpf)).thenReturn(new Employee("John Doe", cpf, "", 300L));

        assertThrows(EntityAlreadyExistsException.class, () -> employeeService.create(createEmployeeDTO));

        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAnEmployeeWithAnEmailThatWasAlreadyUsed() {
        String email = "mail@mail.com";
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO("John Doe", "12345678900", email, 300L);

        when(employeeRepository.findByEmail(email)).thenReturn(new Employee("John Doe", "12345678900", email, 300L));

        assertThrows(EntityAlreadyExistsException.class, () -> employeeService.create(createEmployeeDTO));

        verify(employeeRepository, times(0)).save(any(Employee.class));
    }
}