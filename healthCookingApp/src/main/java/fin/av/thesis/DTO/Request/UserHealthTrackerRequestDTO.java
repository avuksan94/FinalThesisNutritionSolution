package fin.av.thesis.DTO.Request;

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
@AllArgsConstructor
@NoArgsConstructor
public class UserHealthTrackerRequestDTO {
    private String userId;
    private String diet;
    private List<Allergy> knownAllergies;
    private List<HealthCondition> healthConditions;
}