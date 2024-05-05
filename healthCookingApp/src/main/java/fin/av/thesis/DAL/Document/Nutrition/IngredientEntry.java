package fin.av.thesis.DAL.Document.Nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientEntry {
    @Field("ingredient_id")
    private String ingredientId;
    private double quantity;
    private String unit;
    private String notes;

    public IngredientEntry(double quantity, String unit, String notes) {
        this.quantity = quantity;
        this.unit = unit;
        this.notes = notes;
    }
}
