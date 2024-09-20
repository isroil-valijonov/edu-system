package com.example.edusystem.service;

import com.example.edusystem.dto.AttendanceRequestDto;
import com.example.edusystem.entity.Attendance;
import com.example.edusystem.entity.Group;
import com.example.edusystem.entity.Student;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.repository.AttendanceRepository;
import com.example.edusystem.repository.GroupRepository;
import com.example.edusystem.repository.StudentRepository;
import com.example.edusystem.response.AttendanceResponseDto;
import com.example.edusystem.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public CommonResponse createAttendance(AttendanceRequestDto attendanceRequestDto) throws CustomNotFoundException, BadRequestException {
        Student student = studentRepository.findById(attendanceRequestDto.getStudentId())
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));

        Group group = groupRepository.findById(attendanceRequestDto.getGroupId())
                .orElseThrow(() -> new CustomNotFoundException("Group not found"));

        if (!student.getGroups().contains(group)) {
            throw new CustomNotFoundException("Student does not exist in this group");
        }

        boolean attendanceExists = attendanceRepository.existsByStudentAndGroupAndDate(student, group, attendanceRequestDto.getDate());
        if (attendanceExists) {
            throw new BadRequestException("Student's attendance for this day");
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setGroup(group);
        attendance.setAttended(attendanceRequestDto.isAttended());
        attendance.setDate(attendanceRequestDto.getDate());

        attendanceRepository.save(attendance);
        return new CommonResponse(HttpStatus.CREATED.value(), "Attendance successfully created", LocalDateTime.now());
    }

    public AttendanceResponseDto getAttendance(Long id) throws CustomNotFoundException {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Attendance not found"));
        return new AttendanceResponseDto(
                attendance.getId(),
                attendance.getStudent().getId(),
                attendance.getGroup().getId(),
                attendance.isAttended(),
                attendance.getDate());
    }

    public CommonResponse updateAttendance(Long id, AttendanceRequestDto attendanceRequestDto) throws CustomNotFoundException {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Attendance not found"));

        Student student = studentRepository.findById(attendanceRequestDto.getStudentId())
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));
        Group group = groupRepository.findById(attendanceRequestDto.getGroupId())
                .orElseThrow(() -> new CustomNotFoundException("Group not found"));

        attendance.setStudent(student);
        attendance.setGroup(group);
        attendance.setAttended(attendanceRequestDto.isAttended());
        attendance.setDate(attendanceRequestDto.getDate());

        attendanceRepository.save(attendance);
        return new CommonResponse(HttpStatus.OK.value(), "Attendance successfully update", LocalDateTime.now());
    }

    public CommonResponse deleteAttendance(Long id) throws CustomNotFoundException {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Attendance not found"));

        Student student = attendance.getStudent();
        Group group = attendance.getGroup();

        attendanceRepository.delete(attendance);

        updateStudentAttendances(student, attendance);
        updateGroupAttendances(group, attendance);

        return new CommonResponse(HttpStatus.OK.value(), "Attendance successfully deleted", LocalDateTime.now());
    }

    public List<AttendanceResponseDto> getAllAttendances() {
        List<Attendance> attendances = attendanceRepository.findAll();
        return attendances.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponseDto> getStudentAttendances(Long studentId) {
        List<Attendance> attendances = attendanceRepository.findAllByStudentId(studentId);
        return attendances.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AttendanceResponseDto> getGroupAttendances(Long groupId) {
        List<Attendance> attendances = attendanceRepository.findAllByGroupId(groupId);
        return attendances.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AttendanceResponseDto convertToDto(Attendance attendance) {
        return new AttendanceResponseDto(
                attendance.getId(),
                attendance.getStudent().getId(),
                attendance.getGroup().getId(),
                attendance.isAttended(),
                attendance.getDate()
        );
    }

    private void updateStudentAttendances(Student student, Attendance attendance) {
        Set<Attendance> updatedAttendances = student.getAttendances().stream()
                .filter(att -> !att.getId().equals(attendance.getId()))
                .collect(Collectors.toSet());
        student.setAttendances(updatedAttendances);
        studentRepository.save(student);
    }

    private void updateGroupAttendances(Group group, Attendance attendance) {
        Set<Attendance> updatedAttendances = group.getAttendances().stream()
                .filter(att -> !att.getId().equals(attendance.getId()))
                .collect(Collectors.toSet());
        group.setAttendances(updatedAttendances);
        groupRepository.save(group);
    }

}
