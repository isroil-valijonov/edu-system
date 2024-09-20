package com.example.edusystem.service;

import com.example.edusystem.dto.ExamResultRequestDto;
import com.example.edusystem.entity.Exam;
import com.example.edusystem.entity.Student;
import com.example.edusystem.entity.StudentExam;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.repository.ExamRepository;
import com.example.edusystem.repository.StudentExamRepository;
import com.example.edusystem.repository.StudentRepository;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.ExamResultResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentExamService {
    private final StudentExamRepository studentExamRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;

    public ExamResultResponseDto createResult(ExamResultRequestDto resultRequestDto) throws CustomNotFoundException, BadRequestException {

        Student student = studentRepository.findById(resultRequestDto.getStudentId())
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));
        Exam exam = examRepository.findById(resultRequestDto.getExamId())
                .orElseThrow(() -> new CustomNotFoundException("Exam not found"));

        boolean exists = studentExamRepository.existsByStudentAndExam(student, exam);
        if (exists) {
            throw new BadRequestException("The student already has a result for this exam");
        }

        StudentExam examResult = new StudentExam();
        examResult.setStudent(student);
        examResult.setExam(exam);
        examResult.setResult(resultRequestDto.getScore());

        StudentExam savedResult = studentExamRepository.save(examResult);

        return mapToResponseDto(savedResult);
    }

    public ExamResultResponseDto updateResult(Long id, ExamResultRequestDto resultRequestDto) throws CustomNotFoundException, BadRequestException {
        StudentExam examResult = studentExamRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Exam result not found"));

        if (examResult.getResult() == null) {
            throw new BadRequestException("Cannot update result because no score has been assigned to this exam yet.");
        }

        if (!examResult.getExam().getId().equals(resultRequestDto.getExamId()) || !examResult.getStudent().getId().equals(resultRequestDto.getStudentId())) {
            Student student = studentRepository.findById(resultRequestDto.getStudentId())
                    .orElseThrow(() -> new CustomNotFoundException("Student not found"));
            Exam exam = examRepository.findById(resultRequestDto.getExamId())
                    .orElseThrow(() -> new CustomNotFoundException("Exam not found"));

            boolean exists = studentExamRepository.existsByStudentAndExam(student, exam);
            if (exists) {
                throw new BadRequestException("The student already has a result for this exam");
            }

            examResult.setStudent(student);
            examResult.setExam(exam);
        }

        examResult.setResult(resultRequestDto.getScore());

        StudentExam updatedResult = studentExamRepository.save(examResult);
        return mapToResponseDto(updatedResult);
    }


    public CommonResponse deleteResult(Long id) throws CustomNotFoundException {
        StudentExam examResult = studentExamRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Exam result not found"));

        examResult.setStudent(null);
        examResult.setExam(null);

        studentExamRepository.delete(examResult);

        return new CommonResponse(HttpStatus.OK.value(), "Exam result successfully deleted", LocalDateTime.now());
    }

    public ExamResultResponseDto getResult(Long id) throws CustomNotFoundException {
        StudentExam examResult = studentExamRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Exam result not found"));

        return mapToResponseDto(examResult);
    }

    public List<ExamResultResponseDto> getAllResults() {
        List<StudentExam> examResults = studentExamRepository.findAll();
        return examResults.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ExamResultResponseDto> getResultsByStudentId(Long studentId) {
        List<StudentExam> examResults = studentExamRepository.findByStudent_Id(studentId);
        return examResults.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ExamResultResponseDto> getResultsByGroupId(Long groupId) {
        List<StudentExam> examResults = studentExamRepository.findByExam_Group_Id(groupId);
        return examResults.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    private ExamResultResponseDto mapToResponseDto(StudentExam examResult) {
        ExamResultResponseDto responseDto = new ExamResultResponseDto();
        responseDto.setId(examResult.getId());
        responseDto.setStudentId(examResult.getStudent().getId());
        responseDto.setExamId(examResult.getExam().getId());
        responseDto.setScore(examResult.getResult());
        return responseDto;
    }


}
