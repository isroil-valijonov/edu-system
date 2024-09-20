package com.example.edusystem.controller;

import com.example.edusystem.dto.CourseRequestDto;
import com.example.edusystem.entity.Course;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.CourseResponseDto;
import com.example.edusystem.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/create-course")
    public ResponseEntity<CourseResponseDto> createCourse (@RequestBody @Valid CourseRequestDto courseRequestDto) throws CustomException {
        CourseResponseDto course = courseService.createCourse(courseRequestDto);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/update-course/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(@PathVariable Long id, @RequestBody @Valid CourseRequestDto courseUpdateDto) throws CustomNotFoundException, CustomException {
        CourseResponseDto courseResponseDto = courseService.updateCourse(id, courseUpdateDto);
        return ResponseEntity.ok(courseResponseDto);
    }

    @GetMapping("/get-course/{id}")
    public ResponseEntity<CourseResponseDto> getCourse(@Valid @PathVariable Long id) throws CustomNotFoundException {
        CourseResponseDto course = courseService.getCourse(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/all-course")
    public ResponseEntity<List<Course>> allCourse() {
        List<Course> allCourse = courseService.getAllCourse();
        return ResponseEntity.ok(allCourse);
    }

    @DeleteMapping("/delete-course/{id}")
    public ResponseEntity<CommonResponse> deleteCourse(@PathVariable @Valid Long id) throws CustomNotFoundException {
        CommonResponse commonResponse = courseService.deleteCourse(id);
        return ResponseEntity.ok(commonResponse);
    }
}
