package fin.av.thesis.DAL.Document.Nutrition;

import fin.av.thesis.DAL.Enum.Allergy;
import fin.av.thesis.DAL.Enum.HealthCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "user_health_trackers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserHealthTracker {
    @MongoId
    private String id;

    @Field("user_id")
    private String userId;

    @Field("diet")
    private String diet;

    @Field("known_allergies")
    private List<Allergy> knownAllergies;

    @Field("health_conditions")
    private List<HealthCondition> healthConditions;

    @Field("health_warning_id")
    private String healthWarningId;
}
