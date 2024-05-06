package fin.av.thesis.DTO.Shared;

import fin.av.thesis.DAL.Enum.MealType;
import jakarta.validation.Valid;
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
    @Valid
    private RecipeDTO recipe;
}
