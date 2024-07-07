package fin.av.thesis.DTO.Shared;

import fin.av.thesis.DAL.Enum.MealType;
import jakarta.validation.Valid;

public record MealRecipeDTO(
        MealType meal,
        @Valid RecipeDTO recipe
) {}