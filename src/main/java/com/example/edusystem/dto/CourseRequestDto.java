package com.example.edusystem.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {
    @NotEmpty(message = "Course Name cannot be empty!")
    @Column(unique = true)
    private String name;

    @NotNull(message = "Course price cannot be null")
    private int price;
}
