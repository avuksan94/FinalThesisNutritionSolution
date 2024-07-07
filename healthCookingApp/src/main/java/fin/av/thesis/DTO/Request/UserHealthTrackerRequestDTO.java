package fin.av.thesis.DTO.Request;

import fin.av.thesis.DAL.Enum.Allergy;
import fin.av.thesis.DAL.Enum.HealthCondition;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UserHealthTrackerRequestDTO(
        String userId,
        @NotBlank(message = "Diet is required") String diet,
        List<Allergy> knownAllergies,
        List<HealthCondition> healthConditions
) {}