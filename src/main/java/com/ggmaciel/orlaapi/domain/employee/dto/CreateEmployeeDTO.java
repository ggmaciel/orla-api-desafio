package com.ggmaciel.orlaapi.domain.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.*;

@Data
public class CreateEmployeeDTO {

    @NotNull(message = INVALID_NAME_SIZE)
    @Size(min =  1, max = 255, message = INVALID_NAME_SIZE)
    private String name;

    @NotNull(message = INVALID_CPF_SIZE)
    @Size(min = 11, max = 11, message = INVALID_CPF_SIZE)
    private String cpf;

    @NotNull(message = INVALID_EMAIL)
    @Email(message = INVALID_EMAIL)
    @Size(min = 3, max = 255, message = INVALID_EMAIL)
    private String email;

    @PositiveOrZero(message = INVALID_SALARY)
    private double salary;

    public CreateEmployeeDTO(String name, String cpf, String email, double salary) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.salary = salary;
    }
}
