package com.ggmaciel.orlaapi.domain.project.dto;

import jakarta.validation.constraints.Size;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.INVALID_NAME_SIZE;

public class CreateProjectDTO {

    @Size(min =  1, max = 255, message = INVALID_NAME_SIZE)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
