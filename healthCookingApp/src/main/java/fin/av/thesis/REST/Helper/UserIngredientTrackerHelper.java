package fin.av.thesis.REST.Helper;

import fin.av.thesis.DAL.Document.Nutrition.Ingredient;
import fin.av.thesis.DAL.Document.Nutrition.UserIngredientTracker;

public class UserIngredientTrackerHelper {
    public static void updateIngredientTrackerDetails(UserIngredientTracker existingIngredientTracker, UserIngredientTracker updatedIngredientTracker) {
        existingIngredientTracker.setName(updatedIngredientTracker.getName());
        existingIngredientTracker.setUnit(updatedIngredientTracker.getUnit());
        existingIngredientTracker.setQuantity(updatedIngredientTracker.getQuantity());
    }
}
