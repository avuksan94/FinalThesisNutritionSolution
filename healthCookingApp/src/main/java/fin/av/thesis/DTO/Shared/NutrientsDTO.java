package fin.av.thesis.DTO.Shared;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutrientsDTO {
    @NotNull(message = "Calories cannot be null")
    @PositiveOrZero(message = "Calories must be zero or positive")
    private double calories;

    @NotNull(message = "Protein cannot be null")
    @PositiveOrZero(message = "Protein must be zero or positive")
    private double protein;

    @NotNull(message = "Carbs cannot be null")
    @PositiveOrZero(message = "Carbs must be zero or positive")
    private double carbs;

    @NotNull(message = "Fats cannot be null")
    @PositiveOrZero(message = "Fats must be zero or positive")
    private double fats;

    @NotNull(message = "Fiber cannot be null")
    @PositiveOrZero(message = "Fiber must be zero or positive")
    private double fiber;

    @NotNull(message = "Sugar cannot be null")
    @PositiveOrZero(message = "Sugar must be zero or positive")
    private double sugar;
}
