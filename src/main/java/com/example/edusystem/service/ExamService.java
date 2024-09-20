package com.example.edusystem.service;

import com.example.edusystem.dto.ExamRequestDto;
import com.example.edusystem.entity.Exam;
import com.example.edusystem.entity.Group;
import com.example.edusystem.entity.StudentExam;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.repository.ExamRepository;
import com.example.edusystem.repository.GroupRepository;
import com.example.edusystem.repository.StudentExamRepository;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.ExamResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final GroupRepository groupRepository;
    private final StudentExamRepository studentExamRepository;

    public ExamResponseDto createExam(ExamRequestDto examRequestDto) throws CustomNotFoundException, BadRequestException {
        Group group = groupRepository.findById(examRequestDto.getGroupId())
                .orElseThrow(() -> new CustomNotFoundException("Group not found"));

        boolean exists = examRepository.existsByGroupAndDate(group, examRequestDto.getDate());
        if (exists) {
            throw new BadRequestException("Exam with the same date already exists for this group");
        }

        Exam exam = new Exam();
        exam.setSubject(examRequestDto.getSubject());
        exam.setGroup(group);
        exam.setDate(examRequestDto.getDate());

        Exam savedExam = examRepository.save(exam);

        return mapToResponseDto(savedExam);
    }


    public List<ExamResponseDto> getAllExams() {
        List<Exam> exams = examRepository.findAll();
        return exams.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public ExamResponseDto getExam(Long id) throws CustomNotFoundException {
        Exam exam = examRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Exam not found")
        );
        return mapToResponseDto(exam);
    }

    public ExamResponseDto updateExam(Long examId, ExamRequestDto examUpdateDto) throws CustomNotFoundException, BadRequestException {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new CustomNotFoundException("Exam not found"));

        if (examUpdateDto.getDate() != null || examUpdateDto.getGroupId() != null) {
            LocalDateTime newDate = examUpdateDto.getDate() != null ? examUpdateDto.getDate() : exam.getDate();
            Group newGroup = examUpdateDto.getGroupId() != null ?
                    groupRepository.findById(examUpdateDto.getGroupId())
                            .orElseThrow(() -> new CustomNotFoundException("Group not found")) :
                    exam.getGroup();

            boolean exists = examRepository.existsByGroupAndDateAndIdNot(newGroup, newDate, examId);
            if (exists) {
                throw new BadRequestException("Another exam with the same date already exists for this group");
            }

            exam.setGroup(newGroup);
            exam.setDate(newDate);
        }

        if (examUpdateDto.getSubject() != null) {
            exam.setSubject(examUpdateDto.getSubject());
        }

        Exam updatedExam = examRepository.save(exam);

        return mapToResponseDto(updatedExam);
    }


    public CommonResponse deleteExam(Long examId) throws CustomNotFoundException {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new CustomNotFoundException("Exam not found"));

        List<StudentExam> results = studentExamRepository.findByExamId(examId);
        studentExamRepository.deleteAll(results);

        exam.setGroup(null);

        examRepository.delete(exam);
        return new CommonResponse(HttpStatus.OK.value(), "Exam successfully deleted", LocalDateTime.now());
    }

    private ExamResponseDto mapToResponseDto(Exam exam) {
        ExamResponseDto responseDto = new ExamResponseDto();
        responseDto.setId(exam.getId());
        responseDto.setSubject(exam.getSubject());
        responseDto.setGroupId(exam.getGroup().getId());
        responseDto.setDate(exam.getDate());
        return responseDto;
    }
}
