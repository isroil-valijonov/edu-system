package com.example.edusystem.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AverageRatingResponseDto {
    private Long groupId;
    private Float averageRating;
    private Integer totalRatings;
}
