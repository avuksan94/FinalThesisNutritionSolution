package fin.av.thesis.DTO.Shared;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record NutritionSummaryDTO(
        @NotNull(message = "Total calories cannot be null")
        @PositiveOrZero(message = "Total calories must be zero or positive")
        double totalCalories,

        @NotNull(message = "Total protein cannot be null")
        @PositiveOrZero(message = "Total protein must be zero or positive")
        double totalProtein,

        @NotNull(message = "Total carbs cannot be null")
        @PositiveOrZero(message = "Total carbs must be zero or positive")
        double totalCarbs,

        @NotNull(message = "Total fats cannot be null")
        @PositiveOrZero(message = "Total fats must be zero or positive")
        double totalFats,

        @NotNull(message = "Total fiber cannot be null")
        @PositiveOrZero(message = "Total fiber must be zero or positive")
        double totalFiber,

        @NotNull(message = "Total sugar cannot be null")
        @PositiveOrZero(message = "Total sugar must be zero or positive")
        double totalSugar
) {}