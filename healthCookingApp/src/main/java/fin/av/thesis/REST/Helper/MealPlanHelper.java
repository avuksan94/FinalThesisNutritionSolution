package fin.av.thesis.REST.Helper;

import fin.av.thesis.DAL.Document.Nutrition.MealPlan;

public class MealPlanHelper {
    public static void updateMealPlan(MealPlan existingMealPlan, MealPlan updatedMealPlan) {
        existingMealPlan.setMeals(updatedMealPlan.getMeals());
        existingMealPlan.setCreatedAt(updatedMealPlan.getCreatedAt());
    }
}
