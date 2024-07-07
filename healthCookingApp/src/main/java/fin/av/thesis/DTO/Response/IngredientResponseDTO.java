package fin.av.thesis.DTO.Response;

import fin.av.thesis.DTO.Shared.NutrientsDTO;

public record IngredientResponseDTO(
        String id,
        String name,
        NutrientsDTO nutrients
) {}
