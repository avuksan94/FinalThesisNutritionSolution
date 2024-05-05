package fin.av.thesis.DAL.Document.OpenAI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealRecipeResponse {
    private String meal;
    private String title;
    private String description;
    private List<SimpleIngredient> ingredients;
    @JsonProperty("preparation_steps")
    private List<String> preparation;
    @JsonProperty("nutrition_summary")
    private SimpleNutrition nutrition;
    @JsonProperty("cooking_time")
    private int cookingTime;
    private int servings;
    @JsonProperty("special_notes")
    private String notes;
    @JsonProperty("health_warning")
    private String healthWarning;
}
