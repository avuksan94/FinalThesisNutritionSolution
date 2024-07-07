package fin.av.thesis.DTO.Request;

import fin.av.thesis.DTO.Shared.IngredientEntryDTO;
import fin.av.thesis.DTO.Shared.NutritionSummaryDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record RecipeRequestDTO(
        @NotBlank(message = "Title is required") String title,
        String healthTrackerId,
        @NotBlank(message = "Description is required") String description,
        @NotEmpty(message = "At least one ingredient is required") @Valid List<IngredientEntryDTO> ingredients,
        @NotEmpty(message = "Preparation steps are required") List<@NotBlank(message = "Preparation steps cannot be blank") String> preparationSteps,
        @NotNull(message = "Nutrition summary is required") @Valid NutritionSummaryDTO nutritionSummary,
        @Min(value = 1, message = "Cooking time must be at least 1 minute")
        @Max(value = 1440, message = "Cooking time must not exceed 24 hours") int cookingTime,
        @Min(value = 1, message = "There must be at least one serving") int servings,
        @NotBlank(message = "Notes cannot be blank") String notes,
        String healthWarning,
        String language
) {}