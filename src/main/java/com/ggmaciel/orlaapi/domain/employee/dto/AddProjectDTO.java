package com.ggmaciel.orlaapi.domain.employee.dto;

public class AddProjectDTO {

    // TODO add not null
    private Long employeeId;

    // TODO add not null
    private Long projectId;

    public AddProjectDTO(Long employeeId, Long projectId) {
        this.employeeId = employeeId;
        this.projectId = projectId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
