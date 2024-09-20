package com.example.edusystem.service;

import com.example.edusystem.dto.SignUpRequest;
import com.example.edusystem.dto.UserUpdateDto;
import com.example.edusystem.entity.Management;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.UserResponseDto;
import com.example.edusystem.response.UserSignUpResponse;
import com.example.edusystem.entity.Admin;
import com.example.edusystem.entity.Role;
import com.example.edusystem.entity.User;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.repository.AdminRepository;
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
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;

    @Transactional
    public UserSignUpResponse createAdmin(SignUpRequest signUp) throws CustomException {
        User userByPhoneNumber = userRepository.findUserByPhoneNumber(signUp.getPhoneNumber());

        if (userByPhoneNumber != null) {
            throw new UsernameNotFoundException("Admin with this phone number already exists");
        }

        try {
            User user = new User();
            user.setPhoneNumber(signUp.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setRole(Role.ADMIN);

            Admin admin = new Admin();
            admin.setFullName(signUp.getFullName());
            admin.setUser(user);

            user.setAdmin(admin);
            userRepository.save(user);
            adminRepository.save(admin);

            return new UserSignUpResponse(
                    admin.getId(),
                    admin.getFullName(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getRole(),
                    HttpStatus.CREATED.value());
        } catch (Exception e) {
            throw new CustomException("Error occurred while creating admin", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public UserResponseDto updateAdmin(Long id, UserUpdateDto userUpdateDto) throws CustomException, CustomNotFoundException {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isEmpty()) {
            throw new CustomNotFoundException("Admin not found with id: " + id, HttpStatus.NOT_FOUND.value());
        }

        Admin admin = optionalAdmin.get();
        User user = admin.getUser();

        if (user == null) {
            throw new CustomException("User not found for this admin", HttpStatus.NOT_FOUND.value());
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
            admin.setFullName(userUpdateDto.getFullName());
        }

        boolean phoneNumberChanged = !Objects.equals(user.getPhoneNumber(), userUpdateDto.getPhoneNumber());

        if (phoneNumberChanged) {
            user.setPhoneNumber(userUpdateDto.getPhoneNumber());
        }

        userRepository.save(user);
        adminRepository.save(admin);

        String token = phoneNumberChanged ? jwtService.generateToken(user) : null;

        return new UserResponseDto(admin.getId(), admin.getFullName(), user.getPhoneNumber(), user.getPassword(), user.getRole(), token);
    }

    public UserResponseDto getAdmin(Long id) throws CustomException, CustomNotFoundException {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            User user = admin.getUser();
            return new UserResponseDto(
                    admin.getId(),
                    admin.getFullName(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getRole()
            );
        } else {
            throw new CustomNotFoundException("Admin not found", HttpStatus.NOT_FOUND.value());
        }
    }
    public List<UserResponseDto> getAllAdmin() {
        List<Admin> allAdmin = adminRepository.findAll();
        return allAdmin.stream().map(admin -> {
            User user = admin.getUser();
            return new UserResponseDto(
                    admin.getId(),
                    admin.getFullName(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getRole()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public CommonResponse deleteAdmin(Long id) throws CustomNotFoundException {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Admin not found", HttpStatus.NOT_FOUND.value()));

        User user = admin.getUser();
        if (user != null) {
            userRepository.delete(user);
        }

        adminRepository.delete(admin);
        return new CommonResponse(HttpStatus.OK.value(), "Admin deleted successfully", LocalDateTime.now());

    }
}
