package fin.av.thesis.DTO.Shared;

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
    private double quantity;
    private String unit;
    private String notes;
}
