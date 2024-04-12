package com.ggmaciel.orlaapi.domain.project;

import com.ggmaciel.orlaapi.domain.employee.Employee;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(name = "date_of_creation")
    private Date dateOfCreation;

    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees;

    public Project(Long id, String name, Date dateOfCreation, Set<Employee> employees) {
        this.id = id;
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.employees = employees;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
