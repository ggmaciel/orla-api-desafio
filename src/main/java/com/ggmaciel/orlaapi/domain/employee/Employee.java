package com.ggmaciel.orlaapi.domain.employee;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ggmaciel.orlaapi.domain.project.Project;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    @EqualsAndHashCode.Exclude
    private Set<Project> projects;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double salary;

    public Employee(String cpf, String email, String name, double salary) {
        this.cpf = cpf;
        this.email = email;
        this.name = name;
        this.salary = salary;
        this.projects = new HashSet<>();
    }
}
