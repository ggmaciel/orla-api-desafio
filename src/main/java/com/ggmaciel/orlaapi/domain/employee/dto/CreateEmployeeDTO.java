package com.ggmaciel.orlaapi.domain.employee.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.INVALID_CPF_SIZE;
import static com.ggmaciel.orlaapi.helpers.ConstantHelper.INVALID_NAME_SIZE;

public class CreateEmployeeDTO {

    @NotNull(message = INVALID_NAME_SIZE)
    @Size(min =  1, max = 255, message = INVALID_NAME_SIZE)
    private String name;

    @NotNull(message = INVALID_CPF_SIZE)
    @Size(min = 11, max = 11, message = INVALID_CPF_SIZE)
    private String cpf;
    private String email;
    private double salary;

    public CreateEmployeeDTO() {
    }

    public CreateEmployeeDTO(String name, String cpf, String email, double salary) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
