package fin.av.thesis.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNutritionTrackerResponseDTO {
    private String id;
    private String userId;
    private Date forDay;
    private double totalCalories;
    private double totalProtein;
    private double totalCarbs;
    private double totalFats;
    private double totalFiber;
    private double totalSugar;
}
