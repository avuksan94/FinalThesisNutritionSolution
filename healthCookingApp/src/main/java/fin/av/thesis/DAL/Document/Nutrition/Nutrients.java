package fin.av.thesis.DAL.Document.Nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Nutrients {
    private double calories;
    private double protein;
    private double carbs;
    private double fats;
    private double fiber;
    private double sugar;
}