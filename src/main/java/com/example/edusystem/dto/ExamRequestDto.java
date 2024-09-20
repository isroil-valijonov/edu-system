package com.example.edusystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequestDto {
    private String subject;
    private Long groupId;
    private LocalDateTime date;
}
