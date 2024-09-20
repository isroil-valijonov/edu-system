package com.example.edusystem.service;

import com.example.edusystem.dto.SignUpRequest;
import com.example.edusystem.dto.UserUpdateDto;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.UserResponseDto;
import com.example.edusystem.response.UserSignUpResponse;
import com.example.edusystem.entity.Management;
import com.example.edusystem.entity.Role;
import com.example.edusystem.entity.User;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.repository.ManagementRepository;
import com.example.edusystem.repository.UserRepository;
import com.example.edusystem.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ManagementRepository managementRepository;
    private final JwtService jwtService;

    @Transactional
    public UserSignUpResponse createManagement(SignUpRequest sign) throws CustomException {
        try {
            User user = new User();
            user.setPhoneNumber(sign.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(sign.getPassword()));
            user.setRole(Role.MANAGEMENT);

            Management management = new Management();
            management.setFullName(sign.getFullName());
            management.setUser(user);

            user.setManagement(management);
            userRepository.save(user);
            managementRepository.save(management);

            return new UserSignUpResponse(
                    management.getId(),
                    management.getFullName(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getRole(),
                    HttpStatus.CREATED.value());
        } catch (Exception e) {
            throw new CustomException("Error occurred while creating management", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public UserResponseDto updateManagement(Long managementId, UserUpdateDto userUpdateDto) throws CustomException, CustomNotFoundException {
        Optional<Management> managementOptional = managementRepository.findById(managementId);
        if (managementOptional.isEmpty()) {
            throw new CustomNotFoundException("Management not found with id: " + managementId, HttpStatus.NOT_FOUND.value());
        }

        Management management = managementOptional.get();
        User user = management.getUser();

        if (user == null) {
            throw new CustomException("User not found for this management", HttpStatus.NOT_FOUND.value());
        }

        if (userUpdateDto.getPhoneNumber() == null) {
            throw new CustomException("Phone number cannot be null");
        }

        User userByPhoneNumber = userRepository.findUserByPhoneNumber(userUpdateDto.getPhoneNumber());

        if (userByPhoneNumber != null && !userByPhoneNumber.getId().equals(user.getId())) {
            throw new CustomException("User is already registered with this phone number");
        }

        if (userUpdateDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        if (userUpdateDto.getFullName() != null) {
            management.setFullName(userUpdateDto.getFullName());
        }

        boolean phoneNumberChanged = !Objects.equals(user.getPhoneNumber(), userUpdateDto.getPhoneNumber());

        if (phoneNumberChanged) {
            user.setPhoneNumber(userUpdateDto.getPhoneNumber());
        }

        userRepository.save(user);
        managementRepository.save(management);

        String token = phoneNumberChanged ? jwtService.generateToken(user) : null;

        return new UserResponseDto(management.getId(), management.getFullName(), user.getPhoneNumber(), user.getPassword(), user.getRole(), token);
    }

    public CommonResponse deleteManagement(Long id) throws CustomNotFoundException {
        Management management = managementRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Management not found", HttpStatus.NOT_FOUND.value()));

        User user = management.getUser();
        if (user != null) {
            user.setAdmin(null);
        }

        managementRepository.delete(management);
        return new CommonResponse(HttpStatus.OK.value(), "Admin deleted successfully", LocalDateTime.now());
    }

    public UserResponseDto getManagement(Long id) throws CustomNotFoundException {
        Optional<Management> managementOptional = managementRepository.findById(id);
        if (managementOptional.isPresent()) {
            Management management = managementOptional.get();
            User user = management.getUser();
            return new UserResponseDto(management.getId(), management.getFullName(), user.getPhoneNumber(), user.getPassword(), user.getRole());
        } else {
            throw new CustomNotFoundException("Management not found", HttpStatus.NOT_FOUND.value());
        }
    }

}
