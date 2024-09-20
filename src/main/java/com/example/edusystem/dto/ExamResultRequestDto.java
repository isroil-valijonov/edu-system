package com.example.edusystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultRequestDto {
    private Long studentId;
    private Long examId;
    private Integer score;
}
