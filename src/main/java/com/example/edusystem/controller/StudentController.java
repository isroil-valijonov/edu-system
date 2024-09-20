package com.example.edusystem.controller;

import com.example.edusystem.dto.UserUpdateDto;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.StudentResponseDto;
import com.example.edusystem.response.UserResponseDto;
import com.example.edusystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    @PutMapping("/update-student/{id}")
    public ResponseEntity<UserResponseDto> updateTeacher(@PathVariable Long id, @RequestBody @Valid UserUpdateDto userUpdateDto) throws CustomException, CustomNotFoundException {
        UserResponseDto userResponseDto = studentService.updateStudent(id, userUpdateDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/get-student/{id}")
    public ResponseEntity<UserResponseDto> getStudent(@PathVariable @Valid Long id) throws CustomNotFoundException {
        UserResponseDto student = studentService.getStudent(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/all-student")
    public ResponseEntity<List<UserResponseDto>> getAllStudent() {
        List<UserResponseDto> allStudent = studentService.getAllStudent();
        return ResponseEntity.ok(allStudent);
    }

    @DeleteMapping("/delete-student/{id}")
    public ResponseEntity<CommonResponse> deleteStudent(@PathVariable @Valid Long id) throws CustomNotFoundException {
        CommonResponse commonResponse = studentService.deleteStudent(id);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDto>> searchStudents(@RequestParam(name = "query") String query) {
        List<StudentResponseDto> students = studentService.studentSearch(query);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/add-student-to-group/{studentId}/groups/{groupId}")
    public ResponseEntity<StudentResponseDto> addStudentToGroup(@PathVariable Long studentId, @PathVariable Long groupId) throws CustomNotFoundException {
        StudentResponseDto studentDto = studentService.addStudentToGroup(studentId, groupId);
        return ResponseEntity.ok(studentDto);
    }

    @DeleteMapping("/remove-student-from-group/{studentId}/groups/{groupId}")
    public ResponseEntity<StudentResponseDto> removeStudentFromGroup(@PathVariable Long studentId, @PathVariable Long groupId) throws CustomNotFoundException {
        StudentResponseDto studentDto = studentService.removeStudentFromGroup(studentId, groupId);
        return ResponseEntity.ok(studentDto);
    }

    @PutMapping("/change-student-group/{studentId}")
    public ResponseEntity<StudentResponseDto> changeStudentGroup(@PathVariable Long studentId,
                                                                 @RequestParam(name = "old") Long oldGroupId,
                                                                 @RequestParam(name = "new") Long newGroupId) throws CustomNotFoundException, BadRequestException {
        StudentResponseDto studentDto = studentService.changeStudentGroup(studentId, oldGroupId, newGroupId);
        return ResponseEntity.ok(studentDto);
    }

    @GetMapping("/students-by-group-id/{id}")
    public ResponseEntity<List<StudentResponseDto>> getStudentsByGroupId(@PathVariable @Valid Long id) {
        List<StudentResponseDto> studentsByGroupId = studentService.getStudentsByGroupId(id);
        return ResponseEntity.ok(studentsByGroupId);
    }

}
