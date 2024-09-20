package com.example.edusystem.service;

import com.example.edusystem.dto.RatingRequestDto;
import com.example.edusystem.entity.Group;
import com.example.edusystem.entity.Rating;
import com.example.edusystem.entity.Student;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.repository.GroupRepository;
import com.example.edusystem.repository.RatingRepository;
import com.example.edusystem.repository.StudentRepository;
import com.example.edusystem.response.AverageRatingResponseDto;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.RatingResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    public final StudentRepository studentRepository;
    public final GroupRepository groupRepository;

    public RatingResponseDto createRating(RatingRequestDto ratingRequestDto) throws BadRequestException, CustomNotFoundException {
        if (ratingRequestDto.getGrade() <= 0 || ratingRequestDto.getGrade() > 5) {
            throw new BadRequestException("Grade must be between 1 and 5.");
        }

        Student student = studentRepository.findById(ratingRequestDto.getStudentId())
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));
        Group group = groupRepository.findById(ratingRequestDto.getGroupId())
                .orElseThrow(() -> new CustomNotFoundException("Group not found"));

        Rating rating = new Rating();
        rating.setGrade(ratingRequestDto.getGrade());
        rating.setStudent(student);
        rating.setGroup(group);
        rating.setDate(LocalDateTime.now());

        ratingRepository.save(rating);
        return convertToDto(rating);
    }

    public RatingResponseDto updateRating(Long id, RatingRequestDto ratingRequestDto) throws CustomNotFoundException, BadRequestException {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Rating not found"));

        if (ratingRequestDto.getGrade() <= 0 || ratingRequestDto.getGrade() > 5) {
            throw new BadRequestException("Grade must be between 1 and 5.");
        }

        rating.setGrade(ratingRequestDto.getGrade());
        rating.setDate(LocalDateTime.now());

        ratingRepository.save(rating);
        return convertToDto(rating);
    }

    public RatingResponseDto getRating(Long id) throws CustomNotFoundException {
        Rating rating = ratingRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Rating not found")
        );
        return convertToDto(rating);
    }

    public List<RatingResponseDto> getAllRating() {
        List<Rating> ratingAll = ratingRepository.findAll();
        return ratingAll.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CommonResponse deleteRating(Long id) throws CustomNotFoundException {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Rating not found"));

        rating.setStudent(null);
        rating.setGroup(null);

        ratingRepository.delete(rating);
        return new CommonResponse(HttpStatus.OK.value(), "Rating successfully deleted", LocalDateTime.now());
    }

    public AverageRatingResponseDto getRatingByGroupId(Long groupId) throws CustomNotFoundException {
        groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomNotFoundException("Group not found"));

        List<Rating> ratings = ratingRepository.findRatingsByGroupId(groupId);

        if (ratings.isEmpty()) {
            return new AverageRatingResponseDto(groupId, 0.0f, 0);
        }

        double averageRating = ratings.stream().mapToInt(Rating::getGrade).average().orElse(0.0);

        float roundedAverageRating = (float) (Math.round(averageRating * 10.0) / 10.0);

        int totalRatings = ratings.size();

        return new AverageRatingResponseDto(groupId, roundedAverageRating, totalRatings);
    }

    public RatingResponseDto convertToDto(Rating rating) {
        RatingResponseDto ratingResponseDto = new RatingResponseDto();
        ratingResponseDto.setId(rating.getId());
        ratingResponseDto.setGrade(rating.getGrade());
        ratingResponseDto.setStudentId(rating.getStudent().getId());
        ratingResponseDto.setGroupId(rating.getGroup().getId());
        ratingResponseDto.setDate(rating.getDate());

        return ratingResponseDto;
    }
}
