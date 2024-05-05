package fin.av.thesis.REST.Helper;

import fin.av.thesis.DAL.Document.Nutrition.IngredientEntry;
import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import fin.av.thesis.DAL.Document.OpenAI.SimpleRecipeResponse;

import java.util.ArrayList;
import java.util.List;

public class RecipeHelper {
    public static void updateRecipe(Recipe existingRecipe, Recipe updatedRecipe) {
        existingRecipe.setTitle(updatedRecipe.getTitle());
        existingRecipe.setDescription(updatedRecipe.getDescription());
        existingRecipe.setIngredients(updatedRecipe.getIngredients());
        existingRecipe.setPreparationSteps(updatedRecipe.getPreparationSteps());
        existingRecipe.setNutritionSummary(updatedRecipe.getNutritionSummary());
        existingRecipe.setCookingTime(updatedRecipe.getCookingTime());
        existingRecipe.setServings(updatedRecipe.getServings());
        existingRecipe.setNotes(updatedRecipe.getNotes());
        existingRecipe.setHealthWarning(updatedRecipe.getHealthWarning());
    }

}
