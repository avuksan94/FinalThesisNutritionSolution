package fin.av.thesis.DTO.Request;

import fin.av.thesis.DTO.Shared.IngredientEntryDTO;
import fin.av.thesis.DTO.Shared.NutritionSummaryDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private String healthTrackerId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotEmpty(message = "At least one ingredient is required")
    @Valid
    private List<IngredientEntryDTO> ingredients;

    @NotEmpty(message = "Preparation steps are required")
    private List<@NotBlank(message = "Preparation steps cannot be blank") String> preparationSteps;

    @NotNull(message = "Nutrition summary is required")
    @Valid
    private NutritionSummaryDTO nutritionSummary;

    @Min(value = 1, message = "Cooking time must be at least 1 minute")
    @Max(value = 1440, message = "Cooking time must not exceed 24 hours")
    private int cookingTime;

    @Min(value = 1, message = "There must be at least one serving")
    private int servings;

    @NotBlank(message = "Notes cannot be blank")
    private String notes;

    private String healthWarning;

    private String language;
}
