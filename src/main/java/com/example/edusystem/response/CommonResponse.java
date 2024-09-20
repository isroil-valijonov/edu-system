package com.example.edusystem.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {
    private int status;
    private String message;
    private LocalDateTime dateTime;
}
