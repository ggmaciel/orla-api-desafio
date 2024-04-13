package com.ggmaciel.orlaapi.domain.project;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ggmaciel.orlaapi.domain.employee.Employee;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "date_of_creation", nullable = false)
    private Date dateOfCreation;

    @PrePersist
    protected void onCreate() {
        dateOfCreation = new Date();
    }

    @JsonManagedReference
    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees;

    public Project(String name) {
        this.name = name;
    }
}
