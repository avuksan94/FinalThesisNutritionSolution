package fin.av.thesis.DTO.Request;

import fin.av.thesis.DTO.Shared.MealRecipeDTO;

import java.util.Date;
import java.util.List;

public record MealPlanRequestDTO(
        String healthTrackerId,
        Date createdAt,
        List<MealRecipeDTO> meals
) {}