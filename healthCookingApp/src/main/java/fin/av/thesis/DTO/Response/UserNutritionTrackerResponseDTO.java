package fin.av.thesis.DTO.Response;

import java.util.Date;
public record UserNutritionTrackerResponseDTO(
        String id,
        String userId,
        Date forDay,
        double totalCalories,
        double totalProtein,
        double totalCarbs,
        double totalFats,
        double totalFiber,
        double totalSugar
) {}