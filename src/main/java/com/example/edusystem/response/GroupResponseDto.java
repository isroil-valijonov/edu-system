package com.example.edusystem.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDto {
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime create;
    private String courseName;
    private String teacherName;
}
