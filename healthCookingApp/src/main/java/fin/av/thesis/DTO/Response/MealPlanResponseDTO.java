package fin.av.thesis.DTO.Response;

import fin.av.thesis.DTO.Shared.MealRecipeDTO;

import java.util.Date;
import java.util.List;

public record MealPlanResponseDTO(
        String id,
        String healthTrackerId,
        Date createdAt,
        List<MealRecipeDTO> meals
) {}