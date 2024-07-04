package fin.av.thesis.DAL.Document.Nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserIngredientTracker {
    @MongoId
    private String id;

    @Field("user_id")
    private String userId;

    private String name;
    private double quantity;
    private String unit;
}
