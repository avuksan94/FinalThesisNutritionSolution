package fin.av.thesis.DAL.Document.Nutrition;

import fin.av.thesis.DAL.Enum.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealRecipe {
    private MealType meal;;
    private Recipe recipe;
}
