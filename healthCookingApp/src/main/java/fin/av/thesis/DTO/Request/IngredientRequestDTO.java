package fin.av.thesis.DTO.Request;

import fin.av.thesis.DTO.Shared.NutrientsDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequestDTO {
    @NotBlank(message = "Ingredient name is required")
    private String name;
    @Valid
    private NutrientsDTO nutrients;
}
