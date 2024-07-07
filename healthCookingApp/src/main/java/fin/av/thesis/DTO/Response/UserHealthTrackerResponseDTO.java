package fin.av.thesis.DTO.Response;

import fin.av.thesis.DAL.Enum.Allergy;
import fin.av.thesis.DAL.Enum.HealthCondition;

import java.util.List;

public record UserHealthTrackerResponseDTO(
        String id,
        String userId,
        String diet,
        List<Allergy> knownAllergies,
        List<HealthCondition> healthConditions,
        String healthWarningId
) {}