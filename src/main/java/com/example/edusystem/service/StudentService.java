package com.example.edusystem.service;

import com.example.edusystem.dto.SignUpRequest;
import com.example.edusystem.dto.UserUpdateDto;
import com.example.edusystem.entity.*;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.repository.GroupRepository;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.StudentResponseDto;
import com.example.edusystem.response.UserResponseDto;
import com.example.edusystem.response.UserSignUpResponse;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.repository.StudentRepository;
import com.example.edusystem.repository.UserRepository;
import com.example.edusystem.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GroupRepository groupRepository;


    @Transactional
    public UserSignUpResponse createStudent(SignUpRequest signUp) throws CustomException {
        User userByPhoneNumber = userRepository.findUserByPhoneNumber(signUp.getPhoneNumber());

        if (userByPhoneNumber != null) {
            throw new UsernameNotFoundException("Student with this phone number already exists");
        }

        try {
            User user = new User();
            user.setPhoneNumber(signUp.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setRole(Role.STUDENT);

            Student student = new Student();
            student.setFullName(signUp.getFullName());
            student.setUser(user);

            user.setStudent(student);
            userRepository.save(user);
            studentRepository.save(student);

            return new UserSignUpResponse(
                    student.getId(),
                    student.getFullName(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getRole(),
                    HttpStatus.CREATED.value());
        } catch (Exception e) {
            throw new CustomException("Error occurred while creating student", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public UserResponseDto updateStudent(Long id, UserUpdateDto userUpdateDto) throws CustomException, CustomNotFoundException {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Student not found", HttpStatus.NOT_FOUND.value()));
        User user = student.getUser();

        if (user == null) {
            throw new CustomException("User not found for this student", HttpStatus.NOT_FOUND.value());
        }

        if (userUpdateDto.getPhoneNumber() == null || userUpdateDto.getPhoneNumber().isEmpty()) {
            throw new CustomException("Phone number cannot be null or empty");
        }

        User userBy = userRepository.findUserByPhoneNumber(userUpdateDto.getPhoneNumber());
        if (userBy != null && !userBy.getId().equals(user.getId())) {
            throw new CustomException("User is already registered with this phone number");
        }

        if (userUpdateDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        if (userUpdateDto.getFullName() != null) {
            student.setFullName(userUpdateDto.getFullName());
        }

        boolean phoneNumberChanged = !Objects.equals(user.getPhoneNumber(), userUpdateDto.getPhoneNumber());

        if (phoneNumberChanged) {
            user.setPhoneNumber(userUpdateDto.getPhoneNumber());
        }

        userRepository.save(user);
        studentRepository.save(student);

        String token = phoneNumberChanged ? jwtService.generateToken(user) : null;

        return new UserResponseDto(student.getId(), student.getFullName(), user.getPhoneNumber(), user.getPassword(), user.getRole(), token);
    }

    public UserResponseDto getStudent(Long id) throws CustomNotFoundException {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Student not found", HttpStatus.NOT_FOUND.value()));

        User user = student.getUser();
        return new UserResponseDto(
                student.getId(),
                student.getFullName(),
                user.getPhoneNumber(),
                user.getPassword(),
                user.getRole()
        );

    }

    public List<UserResponseDto> getAllStudent() {
        List<Student> allStudent = studentRepository.findAll();
        return allStudent.stream().map(student -> {
            User user = student.getUser();
            return new UserResponseDto(
                    student.getId(),
                    student.getFullName(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getRole()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public CommonResponse deleteStudent(Long id) throws CustomNotFoundException {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Student not found", HttpStatus.NOT_FOUND.value()));

        User user = student.getUser();
        if (user != null) {
            userRepository.delete(user);
        }

        studentRepository.delete(student);
        return new CommonResponse(HttpStatus.OK.value(), "Student deleted successfully", LocalDateTime.now());
    }

    public List<StudentResponseDto> studentSearch(String query) {
        List<Student> byPhoneNumberPart = studentRepository.findByPhoneNumberPart(query);
        return byPhoneNumberPart.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public StudentResponseDto addStudentToGroup(Long studentId, Long groupId) throws CustomNotFoundException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomNotFoundException("Group not found"));

        student.getGroups().add(group);
        studentRepository.save(student);

        return convertToDto(student);
    }

    public StudentResponseDto removeStudentFromGroup(Long studentId, Long groupId) throws CustomNotFoundException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomNotFoundException("Group not found"));

        student.getGroups().remove(group);
        studentRepository.save(student);

        return convertToDto(student);
    }

    public StudentResponseDto changeStudentGroup(Long studentId, Long oldGroupId, Long newGroupId) throws CustomNotFoundException, BadRequestException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));

        if (student.getGroups() == null || student.getGroups().isEmpty()) {
            throw new BadRequestException("Student does not exist in any group");
        }

        Group oldGroup = groupRepository.findById(oldGroupId)
                .orElseThrow(() -> new CustomNotFoundException("Old group not found"));

        Group newGroup = groupRepository.findById(newGroupId)
                .orElseThrow(() -> new CustomNotFoundException("New group not found"));

        if (!student.getGroups().contains(oldGroup)) {
            throw new CustomNotFoundException("Student does not exist int this group");
        }

        student.getGroups().remove(oldGroup);
        student.getGroups().add(newGroup);
        studentRepository.save(student);

        return convertToDto(student);
    }
    public List<StudentResponseDto> getStudentsByGroupId(Long groupId) {
        List<Student> studentsByGroupsId = studentRepository.findStudentsByGroupsId(groupId);
        return studentsByGroupsId.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private StudentResponseDto convertToDto(Student student) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setFullName(student.getFullName());

        Set<Long> groupIds = student.getGroups().stream()
                .map(Group::getId)
                .collect(Collectors.toSet());
        dto.setGroups(groupIds);

        return dto;
    }
}
