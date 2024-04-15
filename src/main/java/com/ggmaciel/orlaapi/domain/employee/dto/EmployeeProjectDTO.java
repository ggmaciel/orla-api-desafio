package com.ggmaciel.orlaapi.domain.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class EmployeeProjectDTO {

    private Long id;
    private String name;
    private Date dateOfCreation;
}
