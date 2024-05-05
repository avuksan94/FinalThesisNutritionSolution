package fin.av.thesis.DTO.Response;

import fin.av.thesis.DAL.Enum.Allergy;
import fin.av.thesis.DAL.Enum.HealthCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserHealthTrackerResponseDTO {
    private String id;
    private String userId;
    private String diet;
    private List<Allergy> knownAllergies;
    private List<HealthCondition> healthConditions;
    private String healthWarningId;
}
