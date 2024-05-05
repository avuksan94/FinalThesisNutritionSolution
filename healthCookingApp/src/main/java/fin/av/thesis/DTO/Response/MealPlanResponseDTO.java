package fin.av.thesis.DTO.Response;

import fin.av.thesis.DTO.Shared.MealRecipeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealPlanResponseDTO {
    private String id;
    private String healthTrackerId;
    private Date createdAt;
    private List<MealRecipeDTO> meals;
}
