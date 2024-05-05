package fin.av.thesis.DTO.Shared;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutritionSummaryDTO {
    @NotNull(message = "Total calories cannot be null")
    @PositiveOrZero(message = "Total calories must be zero or positive")
    private double totalCalories;

    @NotNull(message = "Total protein cannot be null")
    @PositiveOrZero(message = "Total protein must be zero or positive")
    private double totalProtein;

    @NotNull(message = "Total carbs cannot be null")
    @PositiveOrZero(message = "Total carbs must be zero or positive")
    private double totalCarbs;
    
    @NotNull(message = "Total fats cannot be null")
    @PositiveOrZero(message = "Total fats must be zero or positive")
    private double totalFats;

    @NotNull(message = "Total fiber cannot be null")
    @PositiveOrZero(message = "Total fiber must be zero or positive")
    private double totalFiber;

    @NotNull(message = "Total sugar cannot be null")
    @PositiveOrZero(message = "Total sugar must be zero or positive")
    private double totalSugar;
}
