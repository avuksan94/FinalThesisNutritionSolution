package fin.av.thesis.DAL.Document.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleNutrition {
    public int calories;
    public int protein;
    public int carbs;
    public int fats;
    public int fiber;
    public int sugar;
}