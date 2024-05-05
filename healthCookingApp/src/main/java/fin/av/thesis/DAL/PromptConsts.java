package fin.av.thesis.DAL;

public  class PromptConsts {
    public static final String API_URL = "https://api.openai.com/v1/chat/completions";
    //BEST WORKING VERSION FOR RECIPES WITH WARNING
    public static final String HEALTHY_RECIPE_PROMPT_WITH_WARNING_01 = "Please generate a detailed recipe in JSON format, strictly suitable for a %s diet, considering the specific health condition: %s. " +
            "Use ONLY the ingredients provided in the list: [%s], and ensure none of the recipes include any items that are not compliant with the specified diet or allergens related to: %s. Explicitly exclude" +
            " any ingredients that are not suitable for the specified diet, regardless of their presence in the list. Each recipe must be complete and should include: the title, " +
            "description, a list of the provided ingredients with exact quantities in decimal format (not fractions), unit(for measurement), detailed preparation_steps that only involve these ingredients, a nutrition_summary including " +
            "calories, protein, carbs, fats, fiber, and sugar amounts, cooking_time, servings, any special_notes and health_warning that tells the user that if item is dangerous for their health based on allergies or health condition." +
            " The format must adhere to JSON standards without any comments or non-standard elements, and avoid introducing " +
            "any ingredients not explicitly listed as suitable for the specified diet.";

    // this is with the ingredients that the user has at home
    public static final String MEAL_PLAN_PROMPT_WITH_WARNING_01 = "Please generate a detailed meal_plan for one day in JSON format, STRICTLY suitable for a %s diet, considering the specific health condition: %s. " +
            "Use ONLY the ingredients provided in the list: [%s], and ensure none of the recipes include any items that are not compliant with the specified diet or allergens related to: %s. EXPLICITLY exclude" +
            " any ingredients that are not suitable for the specified diet, regardless of their presence in the list. Each recipe must be complete and should include: the meal (breakfast, lunch, dinner), the title, " +
            "description, a list of the provided ingredients with exact quantities in decimal format (not fractions), unit(for measurement), detailed preparation_steps that only involve these ingredients, a nutrition_summary including " +
            "calories, protein, carbs, fats, fiber, and sugar amounts, cooking_time, servings, and health_warning that tells the user that if item is dangerous for their health based on allergies or health condition." +
            " The format must adhere to JSON standards without any comments or non-standard elements, and avoid introducing " +
            "any ingredients not explicitly listed as suitable for the specified diet. Format the output to have 'meals', with the mentioned details.Do not leave out any of these JSON properties!!";

    //this will generate meal plans based on diet and health condition(can only be used without ingredients)
    public static final String MEAL_PLAN_PROMPT_01 = "Please generate a detailed meal_plan for one day in JSON format, using only the ingredients provided appropriate for a %s diet. Given the " +
            "specific health condition: %s and considering allergies: %s. Ensure the meal plan excludes any additional items, allergens, or any ingredients not suitable " +
            "for the specified diet (e.g., no meat for vegetarian diets). Each recipe must strictly include: the meal (breakfast, lunch, dinner), the title, description, a list of the provided ingredients with" +
            " exact quantities in decimal format (not fractions), unit(for measurement), detailed preparation_steps that exclusively use the specified ingredients, a nutrition_summary including calories, protein, carbs, fats, fiber, and sugar amounts," +
            " cooking_time, servings, and any special_notes. The format must adhere to JSON standards without any comments or non-standard elements, ensuring no unauthorized ingredients are introduced.";

    //Free for all
    public static final String CHECK_DIET_COMPATIBILITY = "Analyze whether a %s diet is suitable for someone with the following health conditions: %s and allergies: %s. Format your response in JSON with the following fields:" +
            " 'warning_message' and 'alternative_diet'. If the diet is appropriate and poses no health risks given their conditions and allergies, set both fields to null. If not, provide a 'warning_message' explaining why the " +
            "diet might be harmful and suggest an 'alternative_diet' that would be safer.";

    public static final String CHECK_DIET_COMPATIBILITY_COMMON = "Analyze whether a %s diet is suitable for someone with the following health conditions: %s and allergies: %s. Format your response in JSON with the following fields:" +
            " 'warning_message' and 'alternative_diet'. If the diet is appropriate and poses no health risks given their conditions and allergies, set both fields to null. If not, provide a 'warning_message' explaining why the diet might" +
            " be harmful and suggest an 'alternative_diet' from the following options only: Vegan, Vegetarian, Ketogenic, Paleo, Gluten-Free, Mediterranean, Low-Carb, Dairy-Free. " +
            "This alternative diet should be safer and more suitable for their condition.";


    public static final String CHECK_DIET_COMPATIBILITY_SPOONACULAR = "Evaluate the suitability of the %s diet for someone with these health conditions: %s, and allergies: %s, compared to other potential diets: Gluten-Free, Ketogenic, Vegetarian, Lacto-Vegetarian, Ovo-Vegetarian, " +
            "Vegan, Pescetarian, Paleo, Primal, Whole 30. Determine whether the selected diet is the most suitable or if another diet might be better. Provide a response in JSON format with the following fields only:" +
            " 'warning_message', 'diet_pros', 'diet_cons', and 'alternative_diets'." +
            " If the selected diet poses no health risks given their conditions and allergies," +
            " set the 'warning_message' to null. If not suitable, provide a 'warning_message' that explains why the diet might be harmful, include a list 'diet_pros' detailing the benefits, a list 'diet_cons' discussing the drawbacks,a list 'ingredients_to_avoid' ingredients to avoid, and suggest the best 'alternative_diets' (also a list) from the list that would be safer and more suitable.";


}
