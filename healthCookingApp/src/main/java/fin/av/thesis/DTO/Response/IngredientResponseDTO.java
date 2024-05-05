package fin.av.thesis.DTO.Response;

import fin.av.thesis.DTO.Shared.NutrientsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponseDTO {
    private String id;
    private String name;
    private NutrientsDTO nutrients;
}
