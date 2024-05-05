package fin.av.thesis.DAL.Document.Nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "health_warning")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthWarning {
    @MongoId
    private String id;
    @Field("warning_message")
    private String warningMessage;
    @Field("diet_pros")
    private List<String> dietPros;
    @Field("diet_cons")
    private List<String> dietCons;
    @Field("alternative_diets")
    private List<String> alternativeDiets;
    @Field("ingredients_to_avoid")
    private List<String> ingredientsToAvoid;
}
