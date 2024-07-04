package fin.av.thesis.DTO.Shared;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {
    private String id;
    @NotBlank(message = "Title is required!")
    private String title;
    @NotBlank(message = "Description is required!")
    private String description;
    private String diet;
    private String createdWith;
    @Valid
    private List<IngredientEntryDTO> ingredients;
    @Valid
    private List<String> preparationSteps;
    @Valid
    private NutritionSummaryDTO nutritionSummary;
    @NotNull(message = "Total cooking time cannot be null")
    @PositiveOrZero(message = "Total cooking time must be zero or positive")
    private int cookingTime;
    @NotNull(message = "Servings cannot be null")
    @PositiveOrZero(message = "Servings must be zero or positive")
    private int servings;
    private String notes;
    private String language;
}
