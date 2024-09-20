package com.example.edusystem.controller;

import com.example.edusystem.dto.RatingRequestDto;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.AverageRatingResponseDto;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.RatingResponseDto;
import com.example.edusystem.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/create-rating")
    public ResponseEntity<RatingResponseDto> createRating(@RequestBody @Valid RatingRequestDto ratingRequestDto) throws CustomNotFoundException, BadRequestException {
        RatingResponseDto ratingResponseDto = ratingService.createRating(ratingRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ratingResponseDto);
    }

    @PutMapping("/update-rating/{id}")
    public ResponseEntity<RatingResponseDto> updateRating(@PathVariable Long id, @RequestBody @Valid RatingRequestDto ratingRequestDto) throws CustomNotFoundException, BadRequestException {
        RatingResponseDto ratingResponseDto = ratingService.updateRating(id, ratingRequestDto);
        return ResponseEntity.ok(ratingResponseDto);
    }

    @GetMapping("/get-rating/{id}")
    public ResponseEntity<RatingResponseDto> getRating(@PathVariable Long id) throws CustomNotFoundException {
        RatingResponseDto ratingResponseDto = ratingService.getRating(id);
        return ResponseEntity.ok(ratingResponseDto);
    }

    @GetMapping("/get-all-rating")
    public ResponseEntity<List<RatingResponseDto>> getAllRatings() {
        List<RatingResponseDto> ratingResponseDto = ratingService.getAllRating();
        return ResponseEntity.ok(ratingResponseDto);
    }

    @DeleteMapping("/delete-rating/{id}")
    public ResponseEntity<CommonResponse> deleteRating(@PathVariable Long id) throws CustomNotFoundException {
        CommonResponse response = ratingService.deleteRating(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/group-rating/{groupId}")
    public ResponseEntity<AverageRatingResponseDto> getRatingByGroupId(@PathVariable Long groupId) throws CustomNotFoundException {
        AverageRatingResponseDto averageRating = ratingService.getRatingByGroupId(groupId);
        return ResponseEntity.ok(averageRating);
    }
}

