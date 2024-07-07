package fin.av.thesis.DTO.Response;

public record UserIngredientTrackerResponseDTO(
        String id,
        String userId,
        String name,
        double quantity,
        String unit
) {}
