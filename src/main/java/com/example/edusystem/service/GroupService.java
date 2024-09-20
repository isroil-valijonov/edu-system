package com.example.edusystem.service;

import com.example.edusystem.dto.GroupRequestDto;
import com.example.edusystem.entity.*;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.repository.*;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.GroupResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final AttendanceRepository attendanceRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;

    public GroupResponseDto createGroup(GroupRequestDto groupRequestDto) throws CustomNotFoundException, BadRequestException {
        if (groupRepository.existsByName(groupRequestDto.getName())) {
            throw new BadRequestException("Group name already exists!");
        }
        Course course = courseRepository.findById(groupRequestDto.getCourseId())
                .orElseThrow(() -> new CustomNotFoundException("Course not found", HttpStatus.NOT_FOUND.value()));
        Teacher teacher = teacherRepository.findById(groupRequestDto.getTeacherId())
                .orElseThrow(() -> new CustomNotFoundException("Teacher not found", HttpStatus.NOT_FOUND.value()));

        Group group = new Group();
        group.setName(groupRequestDto.getName());
        group.setStartTime(groupRequestDto.getStartTime());
        group.setEndTime(groupRequestDto.getEndTime());
        group.setCreateDate(LocalDateTime.now());
        group.setCourse(course);
        group.setTeacher(teacher);

        Group savedGroup = groupRepository.save(group);
        return mapToGroupResponseDto(savedGroup);
    }

    public GroupResponseDto updateGroup(Long id, GroupRequestDto groupRequestDto) throws CustomNotFoundException {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Group not found", HttpStatus.NO_CONTENT.value()));

        Course course = courseRepository.findById(groupRequestDto.getCourseId())
                .orElseThrow(() -> new CustomNotFoundException("Course not found", HttpStatus.NOT_FOUND.value()));
        Teacher teacher = teacherRepository.findById(groupRequestDto.getTeacherId())
                .orElseThrow(() -> new CustomNotFoundException("Teacher not found", HttpStatus.NOT_FOUND.value()));

        group.setName(groupRequestDto.getName());
        group.setStartTime(groupRequestDto.getStartTime());
        group.setEndTime(groupRequestDto.getEndTime());
        group.setCourse(course);
        group.setTeacher(teacher);

        Group updatedGroup = groupRepository.save(group);
        return mapToGroupResponseDto(updatedGroup);
    }

    public GroupResponseDto getGroup(Long id) throws CustomNotFoundException {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Group not found", HttpStatus.NOT_FOUND.value()));
        return mapToGroupResponseDto(group);
    }

    public List<GroupResponseDto> getAllGroup() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(this::mapToGroupResponseDto).collect(Collectors.toList());
    }

    public CommonResponse deleteGroup(Long id) throws CustomNotFoundException {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Group not found", HttpStatus.NOT_FOUND.value()));

        if (group.getAttendances() != null && !group.getAttendances().isEmpty()) {
            attendanceRepository.deleteAll(group.getAttendances());
        }

        if (group.getRatings() != null && !group.getRatings().isEmpty()) {
            ratingRepository.deleteAll(group.getRatings());
        }

        if (group.getComments() != null && !group.getComments().isEmpty()) {
            commentRepository.deleteAll(group.getComments());
        }

        if (group.getExams() != null && !group.getExams().isEmpty()) {
            examRepository.deleteAll(group.getExams());
        }

        if (group.getStudents() != null && !group.getStudents().isEmpty()) {
            for (Student student : group.getStudents()) {
                student.getGroups().remove(group);
                studentRepository.save(student);
            }
        }

        group.setTeacher(null);
        group.setCourse(null);

        groupRepository.delete(group);

        return new CommonResponse(HttpStatus.OK.value(), "Group and related entities deleted successfully", LocalDateTime.now());
    }

    private GroupResponseDto mapToGroupResponseDto(Group group) {
        return new GroupResponseDto(
                group.getId(),
                group.getName(),
                group.getStartTime(),
                group.getEndTime(),
                group.getCreateDate(),
                group.getCourse().getName(),
                group.getTeacher().getFullName()
        );
    }

}
