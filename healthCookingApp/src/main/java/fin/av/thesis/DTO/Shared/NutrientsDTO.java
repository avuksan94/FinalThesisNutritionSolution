package fin.av.thesis.DTO.Shared;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record NutrientsDTO(
        @NotNull(message = "Calories cannot be null")
        @PositiveOrZero(message = "Calories must be zero or positive")
        double calories,

        @NotNull(message = "Protein cannot be null")
        @PositiveOrZero(message = "Protein must be zero or positive")
        double protein,

        @NotNull(message = "Carbs cannot be null")
        @PositiveOrZero(message = "Carbs must be zero or positive")
        double carbs,

        @NotNull(message = "Fats cannot be null")
        @PositiveOrZero(message = "Fats must be zero or positive")
        double fats,

        @NotNull(message = "Fiber cannot be null")
        @PositiveOrZero(message = "Fiber must be zero or positive")
        double fiber,

        @NotNull(message = "Sugar cannot be null")
        @PositiveOrZero(message = "Sugar must be zero or positive")
        double sugar
) {}