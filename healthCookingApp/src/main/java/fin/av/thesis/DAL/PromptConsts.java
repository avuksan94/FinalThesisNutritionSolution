package fin.av.thesis.DAL;

public  class PromptConsts {
    public static final String API_URL = "https://api.openai.com/v1/chat/completions";
    //BEST WORKING VERSION FOR RECIPES WITH WARNING
    public static final String HEALTHY_RECIPE_PROMPT_WITH_WARNING_01 = "Please generate a detailed recipe in JSON format, strictly suitable for a %s diet, considering the specific health condition: %s. " +
            "Use ONLY the ingredients provided in the list: [%s], and ensure none of the recipes include any items that are not compliant with the specified diet or allergens related to: %s. Explicitly exclude" +
            " any ingredients that are not suitable for the specified diet, regardless of their presence in the list. Each recipe must be complete and should include: the title, " +
            "description, a list of the provided ingredients with exact quantities in decimal format (not fractions), unit(for measurement), detailed preparation_steps that only involve these ingredients, a nutrition_summary including " +
            "calories, protein, carbs, fats, fiber, and sugar amounts, cooking_time, servings, any special_notes and health_warning(If the recipe could harm the user based on the health conditions or allergies that he has, please warn the user that he needs to consult a doctor!) that tells the user that if item is dangerous for their health based on allergies or health condition." +
            " The format must adhere to JSON standards without any comments or non-standard elements, and avoid introducing " +
            "any ingredients not explicitly listed as suitable for the specified diet." +
            "(if no health warning or notes exist set their values to null)!";

    public static final String HEALTHY_RECIPE_PROMPT_WITH_WARNING_01_HR = "Molimo generirajte detaljan recept u JSON formatu na hrvatskom jeziku, strogo prikladan za %s dijetu, uzimajući u obzir specifično zdravstveno stanje: %s. " +
            "Koristite SAMO sastojke navedene na popisu: [%s], i osigurajte da recepti ne sadrže nikakve sastojke koji nisu u skladu sa specificiranom dijetom ili alergenima povezanim sa: %s. Izričito isključite " +
            "sve sastojke koji nisu prikladni za specificiranu dijetu, bez obzira na njihovu prisutnost na popisu. Svaki recept mora biti potpun i treba uključivati: title, " +
            "description, list provided ingredients with exact quantities in decimal format (not fractions), unit (for measurement), detailed preparation_steps that only involve these ingredients, a nutrition_summary including " +
            "calories, protein, carbs, fats, fiber, and sugar amounts, cooking_time, servings, any special_notes and health_warning(Ako recept može naštetiti korisniku na temelju zdravstvenih stanja ili alergija koje korisnik ima, molim upozorite korisnika da se treba posavjetovati s liječnikom!) that tells the user if the item is dangerous for their health based on allergies or health condition." +
            " Format mora biti u skladu s JSON standardima bez ikakvih komentara ili nestandardnih elemenata, i izbjegavati uvođenje " +
            "bilo kojih sastojaka koji nisu izričito navedeni kao prikladni za specificiranu dijetu." +
            "(ako ne postoji zdravstveno upozorenje ili napomene postavite njihove vrijednosti na null)!";

    // this is with the ingredients that the user has at home
    public static final String MEAL_PLAN_PROMPT_WITH_WARNING_01 = "Please generate a detailed meal_plan for one day in JSON format, STRICTLY suitable for a %s diet, considering the specific health condition: %s. " +
            "Use ONLY the ingredients provided in the list: [%s], and ensure none of the recipes include any items that are not compliant with the specified diet or allergens related to: %s. EXPLICITLY exclude" +
            " any ingredients that are not suitable for the specified diet, regardless of their presence in the list. Each recipe must be complete and should include: the meal (breakfast, lunch, dinner), the title, " +
            "description, a list of the provided ingredients with exact quantities in decimal format (not fractions), unit(for measurement), detailed preparation_steps that only involve these ingredients, a nutrition_summary including " +
            "calories, protein, carbs, fats, fiber, and sugar amounts, cooking_time, servings, and health_warning that tells the user that if item is dangerous for their health based on allergies or health condition." +
            " The format must adhere to JSON standards without any comments or non-standard elements, and avoid introducing " +
            "any ingredients not explicitly listed as suitable for the specified diet. " +
            "Format the output to have 'meals', with the mentioned details.Do not leave out any of these JSON properties,and also include any health warnings and notes " +
            "(if no health warning or notes exist set their values to null)!!" +
            "Avoid introducing any ingredients not explicitly listed as suitable for the specified diet.";

    public static final String MEAL_PLAN_PROMPT_WITH_WARNING_01_HR = "Please generate a detailed meal_plan for one day in JSON format, STRICTLY suitable for a %s diet, considering the specific health condition: %s. " +
            "Use ONLY the ingredients provided in the list: [%s], and ensure none of the recipes include any items that are not compliant with the specified diet or allergens related to: %s. EXPLICITLY exclude" +
            " any ingredients that are not suitable for the specified diet, regardless of their presence in the list. Each recipe must be complete and should include: the meal (breakfast, lunch, dinner), the title, " +
            "description, a list of the provided ingredients with exact quantities in decimal format (not fractions), unit(for measurement), detailed preparation_steps that only involve these ingredients, a nutrition_summary including " +
            "calories, protein, carbs, fats, fiber, and sugar amounts, cooking_time, servings, and health_warning that tells the user that if item is dangerous for their health based on allergies or health condition." +
            " The format must adhere to JSON standards without any comments or non-standard elements, and avoid introducing " +
            "any ingredients not explicitly listed as suitable for the specified diet. " +
            "Format the output to have 'meals', with the mentioned details.Do not leave out any of these JSON properties,and also include any health warnings and notes " +
            "(if no health warning or notes exist set their values to null)!!" +
            "Avoid introducing any ingredients not explicitly listed as suitable for the specified diet OR ingredients not listed by the user." +
            "(meal can be BREAKFAST,LUNCH and DINNER as it is an Enum) but ALL THE OTHER TEXT OF VALUES MUST BE IN THE CROATIAN LANGUAGE!!";


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

    public static final String CHECK_DIET_COMPATIBILITY_SPOONACULAR = "Evaluate the suitability of the %s diet for someone with these health conditions: %s, and allergies: %s, compared to other potential diets: " +
            "Vegan, Vegetarian, Ketogenic, " +
            "Paleo, Gluten-free, Mediterranean, Low-carb, Dairy-free. Determine whether the selected diet is the most suitable or if another diet might be better. Provide a response in JSON format with the following fields only:" +
            " 'warning_message', 'diet_pros', 'diet_cons', and 'alternative_diets'." +
            " If the selected diet poses no health risks given their conditions and allergies," +
            " set the 'warning_message' to null. If not suitable, provide a 'warning_message' that explains why the diet might be harmful, include a list 'diet_pros' detailing the benefits, a list 'diet_cons' discussing " +
            "the drawbacks,a list 'ingredients_to_avoid' ingredients to avoid, and suggest the best 'alternative_diets' (also a list) from the list that would be safer and more suitable.";
    public static final String CHECK_DIET_COMPATIBILITY_SPOONACULAR_HR = "Evaluate the suitability of the %s diet for someone with these health conditions: %s, and allergies: %s, compared to other potential diets: " +
            "Vegan, Vegetarian, Ketogenic, " +
            "Paleo, Gluten-free, Mediterranean, Low-carb, Dairy-free. Determine whether the selected diet is the most suitable or if another diet might be better. Provide a response in JSON format(in the Croatian language) with the following fields only:" +
            " 'warning_message', 'diet_pros', 'diet_cons', and 'alternative_diets'." +
            " If the selected diet poses no health risks given their conditions and allergies," +
            " set the 'warning_message' to null. If not suitable, provide a 'warning_message' that explains why the diet might be harmful, include a list 'diet_pros' detailing the benefits, a list 'diet_cons' discussing " +
            "the drawbacks,a list 'ingredients_to_avoid' ingredients to avoid, and suggest the best 'alternative_diets' (also a list) from the list that would be safer and more suitable.";

}
