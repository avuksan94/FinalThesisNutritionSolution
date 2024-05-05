package fin.av.thesis.DAL.EnumMapping;

import fin.av.thesis.DAL.Enum.CommonDietType;
import fin.av.thesis.DAL.Enum.SpoonacularDietType;

import java.util.EnumMap;
import java.util.Map;

public class SpoonacularUrlMapping {
    private static final Map<SpoonacularDietType, String> DIET_URLS;

    static {
        DIET_URLS = new EnumMap<>(SpoonacularDietType.class);
        DIET_URLS.put(SpoonacularDietType.VEGAN, "https://www.healthline.com/nutrition/vegan-diet-guide#about-the-vegan-diet");
        DIET_URLS.put(SpoonacularDietType.VEGETERIAN, "https://www.mayoclinic.org/healthy-lifestyle/nutrition-and-healthy-eating/in-depth/vegetarian-diet/art-20046446");
        DIET_URLS.put(SpoonacularDietType.KETOGENIC, "https://www.healthline.com/nutrition/ketogenic-diet-101#diet-types");
        DIET_URLS.put(SpoonacularDietType.PALEO, "https://www.mayoclinic.org/healthy-lifestyle/nutrition-and-healthy-eating/in-depth/paleo-diet/art-20111182");
        DIET_URLS.put(SpoonacularDietType.GLUTEN_FREE, "https://www.mayoclinic.org/healthy-lifestyle/nutrition-and-healthy-eating/in-depth/gluten-free-diet/art-20048530");
        DIET_URLS.put(SpoonacularDietType.LACTO_VEGETERIAN, "https://www.cookunity.com/blog/what-is-lacto-vegetarian");
        DIET_URLS.put(SpoonacularDietType.OVO_VEGETERIAN, "https://www.healthline.com/nutrition/ovo-vegetarian-diet#:~:text=What%20is%20an%20ovo%2Dvegetarian,certain%20baked%20goods%20are%20permitted.");
        DIET_URLS.put(SpoonacularDietType.PESCETARIAN, "https://www.healthline.com/nutrition/pescatarian-diet#:~:text=A%20pescatarian%20is%20someone%20who,poultry%2C%20but%20still%20eat%20fish.");
        DIET_URLS.put(SpoonacularDietType.PRIMAL, "https://www.healthline.com/nutrition/primal-diet");
        DIET_URLS.put(SpoonacularDietType.WHOLE_30, "https://www.forbes.com/health/nutrition/diet/what-is-whole30/#:~:text=While%20on%20the%20Whole30%20program,think%20meats%2C%20fruits%20and%20vegetables.");
    }

    public static String getUrlForDiet(SpoonacularDietType dietType) {
        return DIET_URLS.get(dietType);
    }
}
