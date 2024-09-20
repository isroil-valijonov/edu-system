package com.example.edusystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequestDto {
    private int grade;
    private Long studentId;
    private Long groupId;
}
