package fin.av.thesis.DTO.Request;

import fin.av.thesis.DTO.Shared.NutrientsDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IngredientRequestDTO(
        @NotBlank(message = "Ingredient name is required")
        @Size(max = 50, message = "Ingredient name must be at most 50 characters") String name,
        @Valid NutrientsDTO nutrients
) {}
