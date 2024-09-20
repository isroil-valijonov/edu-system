package com.example.edusystem.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponseDto {
    private Long id;
    private int grade;
    private Long studentId;
    private Long groupId;
    private LocalDateTime date;
}
