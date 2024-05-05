package fin.av.thesis.DTO.Shared;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {
    private String id;
    private String title;
    private String description;
    private List<IngredientEntryDTO> ingredients;
    private List<String> preparationSteps;
    private NutritionSummaryDTO nutritionSummary;
    private int cookingTime;
    private int servings;
    private String notes;
}
