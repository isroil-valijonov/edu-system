package com.example.edusystem.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDto {
    private Long id;
    private Long studentId;
    private Long groupId;
    private boolean attended;
    private LocalDate date;
}
