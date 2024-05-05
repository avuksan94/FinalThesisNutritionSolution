package fin.av.thesis.DTO.Request;

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
public class MealPlanRequestDTO {
    private String healthTrackerId;
    private Date createdAt;
    private List<MealRecipeDTO> meals;
}
