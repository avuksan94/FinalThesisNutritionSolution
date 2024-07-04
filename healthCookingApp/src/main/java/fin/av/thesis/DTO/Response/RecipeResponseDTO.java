package fin.av.thesis.DTO.Response;

import fin.av.thesis.DTO.Shared.IngredientEntryDTO;
import fin.av.thesis.DTO.Shared.NutritionSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponseDTO {
    private String id;
    private String title;
    private String description;
    private String diet;
    private String createdWith;
    private List<IngredientEntryDTO> ingredients;
    private List<String> preparationSteps;
    private NutritionSummaryDTO nutritionSummary;
    private int cookingTime;
    private int servings;
    private String notes;
    private String healthWarning;
    private String language;
}
