package fin.av.thesis.DTO.Response;

import fin.av.thesis.DTO.Shared.IngredientEntryDTO;
import fin.av.thesis.DTO.Shared.NutritionSummaryDTO;

import java.util.List;

public record RecipeResponseDTO(
        String id,
        String title,
        String description,
        String diet,
        String createdWith,
        List<IngredientEntryDTO> ingredients,
        List<String> preparationSteps,
        NutritionSummaryDTO nutritionSummary,
        int cookingTime,
        int servings,
        String notes,
        String healthWarning,
        String language
) {}