package fin.av.thesis.DTO.Shared;

import fin.av.thesis.DAL.Enum.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealRecipeDTO {
    private MealType meal;
    private RecipeDTO recipe;
}
