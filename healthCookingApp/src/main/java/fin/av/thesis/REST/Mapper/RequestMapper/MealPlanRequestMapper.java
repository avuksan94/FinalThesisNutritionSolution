package fin.av.thesis.REST.Mapper.RequestMapper;

import fin.av.thesis.DAL.Document.Nutrition.MealPlan;
import fin.av.thesis.DTO.Request.MealPlanRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MealPlanRequestMapper {
    @Mapping(source = "meals", target = "meals")
    MealPlan DTOMealPlanReqToMealPlan(MealPlanRequestDTO dtoMealPlan);
    @Mapping(source = "meals", target = "meals")
    MealPlanRequestDTO MealPlanToDTOMealPlanReq(MealPlan mealPlan);
}
