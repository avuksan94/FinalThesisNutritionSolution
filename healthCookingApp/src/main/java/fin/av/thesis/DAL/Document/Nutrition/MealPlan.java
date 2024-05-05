package fin.av.thesis.DAL.Document.Nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

@Document(collection = "meal_plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealPlan {
    @MongoId
    private String id;
    @Field("health_tracker_id")
    private String healthTrackerId;
    @Field("created_at")
    private Date createdAt;
    private List<MealRecipe> meals;
}
