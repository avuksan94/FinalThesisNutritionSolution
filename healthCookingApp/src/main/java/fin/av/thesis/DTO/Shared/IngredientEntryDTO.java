package fin.av.thesis.DTO.Shared;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientEntryDTO {
    private String ingredientId;
    private String name;
    @Min(value = 0, message = "Quantity must be non-negative!")
    private double quantity;
    @NotBlank(message = "Unit is required!")
    private String unit;
    private String notes;
}
