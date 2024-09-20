package com.example.edusystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDto {
    private Long studentId;
    private Long groupId;
    private boolean attended;
    private LocalDate date;
}
