package com.example.edusystem.controller;


import com.example.edusystem.dto.AttendanceRequestDto;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.AttendanceResponseDto;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/create-attendance")
    public ResponseEntity<CommonResponse> createAttendance(@RequestBody AttendanceRequestDto attendanceRequestDto) throws CustomNotFoundException, BadRequestException {
        CommonResponse attendance = attendanceService.createAttendance(attendanceRequestDto);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/get-attendance/{id}")
    public ResponseEntity<AttendanceResponseDto> getAttendance(@PathVariable Long id) throws CustomNotFoundException {
        AttendanceResponseDto attendance = attendanceService.getAttendance(id);
        return ResponseEntity.ok(attendance);
    }

    @PutMapping("/update-attendance/{id}")
    public ResponseEntity<CommonResponse> updateAttendance(@PathVariable Long id, @RequestBody AttendanceRequestDto attendanceRequestDto) throws CustomNotFoundException {
        CommonResponse commonResponse = attendanceService.updateAttendance(id, attendanceRequestDto);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("/delete-attendance/{id}")
    public ResponseEntity<CommonResponse> deleteAttendance(@PathVariable Long id) throws CustomNotFoundException {
        CommonResponse commonResponse = attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/get-all-attendance")
    public ResponseEntity<List<AttendanceResponseDto>> getAllAttendances() {
        List<AttendanceResponseDto> allAttendances = attendanceService.getAllAttendances();
        return ResponseEntity.ok(allAttendances);
    }

    @GetMapping("/get-student-id-attendance/{id}")
    public ResponseEntity<List<AttendanceResponseDto>> getStudentIdAttendances(@PathVariable Long id) {
        List<AttendanceResponseDto> allAttendances = attendanceService.getStudentAttendances(id);
        return ResponseEntity.ok(allAttendances);
    }

    @GetMapping("/get-group-id-attendance/{id}")
    public ResponseEntity<List<AttendanceResponseDto>> getGroupIdAttendances(@PathVariable Long id) {
        List<AttendanceResponseDto> allAttendances = attendanceService.getGroupAttendances(id);
        return ResponseEntity.ok(allAttendances);
    }

}
