package fin.av.thesis.REST.Mapper.ResponseMapper;

import fin.av.thesis.DAL.Document.Nutrition.MealPlan;
import fin.av.thesis.DTO.Response.MealPlanResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MealPlanResponseMapper {
    @Mapping(source = "meals", target = "meals")
    MealPlanResponseDTO MealPlanToDTOMealPlanRes(MealPlan mealPlan);
    @Mapping(source = "meals", target = "meals")
    MealPlan DTOMealPlanResToMealPlan(MealPlanResponseDTO dtoRecipe);
}
