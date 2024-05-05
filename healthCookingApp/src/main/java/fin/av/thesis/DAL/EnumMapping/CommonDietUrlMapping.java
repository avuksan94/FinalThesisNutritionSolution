package fin.av.thesis.DAL.EnumMapping;

import fin.av.thesis.DAL.Enum.CommonDietType;

import java.util.EnumMap;
import java.util.Map;

public class CommonDietUrlMapping {
    private static final Map<CommonDietType, String> DIET_URLS;

    static {
        DIET_URLS = new EnumMap<>(CommonDietType.class);
        DIET_URLS.put(CommonDietType.VEGAN, "https://www.healthline.com/nutrition/vegan-diet-guide#about-the-vegan-diet");
        DIET_URLS.put(CommonDietType.VEGETARIAN, "https://www.mayoclinic.org/healthy-lifestyle/nutrition-and-healthy-eating/in-depth/vegetarian-diet/art-20046446");
        DIET_URLS.put(CommonDietType.KETOGENIC, "https://www.healthline.com/nutrition/ketogenic-diet-101#diet-types");
        DIET_URLS.put(CommonDietType.PALEO, "https://www.mayoclinic.org/healthy-lifestyle/nutrition-and-healthy-eating/in-depth/paleo-diet/art-20111182");
        DIET_URLS.put(CommonDietType.GLUTEN_FREE, "https://www.mayoclinic.org/healthy-lifestyle/nutrition-and-healthy-eating/in-depth/gluten-free-diet/art-20048530");
        DIET_URLS.put(CommonDietType.MEDITERRANEAN, "https://www.healthline.com/nutrition/mediterranean-diet-meal-plan#what-is-it");
        DIET_URLS.put(CommonDietType.LOW_CARB, "https://www.healthline.com/nutrition/low-carb-diet-meal-plan-and-menu");
        DIET_URLS.put(CommonDietType.DAIRY_FREE, "https://www.verywellhealth.com/dairy-free-diet-5216769");
    }

    public static String getUrlForDiet(CommonDietType dietType) {
        return DIET_URLS.get(dietType);
    }
}
