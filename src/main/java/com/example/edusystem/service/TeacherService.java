package com.example.edusystem.service;

import com.example.edusystem.dto.SignUpRequest;
import com.example.edusystem.dto.UserUpdateDto;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.UserResponseDto;
import com.example.edusystem.response.UserSignUpResponse;
import com.example.edusystem.entity.Role;
import com.example.edusystem.entity.Teacher;
import com.example.edusystem.entity.User;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.repository.TeacherRepository;
import com.example.edusystem.repository.UserRepository;
import com.example.edusystem.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserSignUpResponse createTeacher(SignUpRequest signUp) throws CustomException {
        User userByPhoneNumber = userRepository.findUserByPhoneNumber(signUp.getPhoneNumber());

        if (userByPhoneNumber != null) {
            throw new UsernameNotFoundException("Teacher with this phone number already exists");
        }

        try {
            User user = new User();
            user.setPhoneNumber(signUp.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setRole(Role.TEACHER);

            Teacher teacher = new Teacher();
            teacher.setFullName(signUp.getFullName());
            teacher.setUser(user);

            user.setTeacher(teacher);
            userRepository.save(user);
            teacherRepository.save(teacher);

            return new UserSignUpResponse(
                    teacher.getId(),
                    teacher.getFullName(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getRole(),
                    HttpStatus.CREATED.value());
        } catch (Exception e) {
            throw new CustomException("Error occurred while creating teacher", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public UserResponseDto updateTeacher(Long id, UserUpdateDto userUpdateDto) throws CustomException, CustomNotFoundException {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Teacher not found", HttpStatus.NOT_FOUND.value()));

        User user = teacher.getUser();

        if (user == null) {
            throw new CustomException("User not found for this admin", HttpStatus.BAD_REQUEST.value());
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
            teacher.setFullName(userUpdateDto.getFullName());
        }

        boolean phoneNumberChanged = !Objects.equals(user.getPhoneNumber(), userUpdateDto.getPhoneNumber());

        if (phoneNumberChanged) {
            user.setPhoneNumber(userUpdateDto.getPhoneNumber());
        }

        userRepository.save(user);
        teacherRepository.save(teacher);

        String token = phoneNumberChanged ? jwtService.generateToken(user) : null;

        return new UserResponseDto(teacher.getId(), teacher.getFullName(), user.getPhoneNumber(), user.getPassword(), user.getRole(), token);
    }

    public UserResponseDto getTeacher(Long id) throws CustomNotFoundException {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            User user = teacher.getUser();
            return new UserResponseDto(
                    teacher.getId(),
                    teacher.getFullName(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getRole()
            );
        } else {
            throw new CustomNotFoundException("Teacher not found", HttpStatus.NOT_FOUND.value());
        }
    }

    public List<UserResponseDto> getAllTeacher() {
        List<Teacher> allTeacher = teacherRepository.findAll();
        return allTeacher.stream().map(teacher -> {
            User user = teacher.getUser();
            return new UserResponseDto(
                    teacher.getId(),
                    teacher.getFullName(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getRole()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public CommonResponse deleteTeacher(Long id) throws CustomNotFoundException {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Teacher not found", HttpStatus.NOT_FOUND.value()));

        User user = teacher.getUser();
        if (user != null) {
            userRepository.delete(user);
        }

        teacherRepository.delete(teacher);
        return new CommonResponse(HttpStatus.OK.value(), "Teacher deleted successfully", LocalDateTime.now());
    }
}
