package com.example.edusystem.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultResponseDto {
    private Long id;
    private Long studentId;
    private Long examId;
    private Integer score;
}
