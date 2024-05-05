package fin.av.thesis.DAL.Document.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealPlanResponse {
    private List<MealRecipeResponse> meals;
}
