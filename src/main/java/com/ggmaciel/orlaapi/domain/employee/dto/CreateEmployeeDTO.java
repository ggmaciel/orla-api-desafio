package com.ggmaciel.orlaapi.domain.employee.dto;

public class CreateEmployeeDTO {
    private String name;
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
