package fin.av.thesis.DTO.Shared;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

public record RecipeDTO(
        String id,
        @NotBlank(message = "Title is required!") String title,
        @NotBlank(message = "Description is required!") String description,
        String diet,
        String createdWith,
        @Valid List<IngredientEntryDTO> ingredients,
        @Valid List<String> preparationSteps,
        @Valid NutritionSummaryDTO nutritionSummary,
        @NotNull(message = "Total cooking time cannot be null")
        @PositiveOrZero(message = "Total cooking time must be zero or positive") int cookingTime,
        @NotNull(message = "Servings cannot be null")
        @PositiveOrZero(message = "Servings must be zero or positive") int servings,
        String notes,
        String language
) {}
