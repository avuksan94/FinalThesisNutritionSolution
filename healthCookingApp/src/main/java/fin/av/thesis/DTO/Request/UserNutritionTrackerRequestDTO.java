package fin.av.thesis.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNutritionTrackerRequestDTO {
    private String userId;
    private Date forDay;
    private double totalCalories;
    private double totalProtein;
    private double totalCarbs;
    private double totalFats;
    private double totalFiber;
    private double totalSugar;
}
