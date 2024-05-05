package fin.av.thesis.DAL.Document.Nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutritionSummary {
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
