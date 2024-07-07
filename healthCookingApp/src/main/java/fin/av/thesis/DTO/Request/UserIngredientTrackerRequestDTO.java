package fin.av.thesis.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserIngredientTrackerRequestDTO(
        @NotBlank(message = "Ingredient name is required")
        @Size(max = 50, message = "Ingredient name must be at most 50 characters") String name,

        @Min(value = 0, message = "Quantity must be non-negative!")double quantity,
        @NotBlank(message = "Unit is required!") String unit
) {}
