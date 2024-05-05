package fin.av.thesis.DAL.Document.Nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.rmi.server.ObjID;
import java.util.List;

@Document(collection = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @MongoId
    private String id;

    @Field("health_tracker_id")
    private String healthTrackerId;

    private String title;
    private String description;

    private List<IngredientEntry> ingredients;
    private List<String> preparationSteps;

    @Field("nutrition_summary")
    private NutritionSummary nutritionSummary;

    @Field("cooking_time")
    private int cookingTime;

    private int servings;
    private String notes;

    @Field("health_warning")
    private String healthWarning;
}