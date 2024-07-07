package fin.av.thesis.DTO.Request;

import java.util.Date;

public record UserNutritionTrackerRequestDTO(
        String userId,
        Date forDay,
        double totalCalories,
        double totalProtein,
        double totalCarbs,
        double totalFats,
        double totalFiber,
        double totalSugar
) {}