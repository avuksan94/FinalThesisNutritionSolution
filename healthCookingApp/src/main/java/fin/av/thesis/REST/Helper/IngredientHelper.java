package fin.av.thesis.REST.Helper;

import fin.av.thesis.DAL.Document.Nutrition.Ingredient;

public class IngredientHelper {
    public static void updateIngredientDetails(Ingredient existingIngredient, Ingredient updatedIngredient) {
        existingIngredient.setName(updatedIngredient.getName());
        existingIngredient.setNutrients(updatedIngredient.getNutrients());
    }
}
