package fin.av.thesis.DAL.Document.Nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document(collection = "user_nutrition_trackers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNutritionTracker {
    @MongoId
    private String id;

    @Field("user_id")
    private String userId;

    @Field("for_day")
    private Date forDay;

    @Field("total_calories")
    private double totalCalories;
    @Field("total_protein")
    private double totalProtein;
    @Field("total_carbs")
    private double totalCarbs;
    @Field("total_fats")
    private double totalFats;
    @Field("total_fiber")
    private double totalFiber;
    @Field("total_sugar")
    private double totalSugar;
}
