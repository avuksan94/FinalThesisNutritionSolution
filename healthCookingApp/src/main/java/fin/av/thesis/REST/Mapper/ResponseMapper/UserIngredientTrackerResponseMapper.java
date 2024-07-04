package fin.av.thesis.REST.Mapper.ResponseMapper;

import fin.av.thesis.DAL.Document.Nutrition.UserIngredientTracker;
import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import fin.av.thesis.DTO.Response.UserIngredientTrackerResponseDTO;
import fin.av.thesis.DTO.Response.UserNutritionTrackerResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserIngredientTrackerResponseMapper {
    UserIngredientTrackerResponseDTO UserIToDTOUserITRes(UserIngredientTracker userIT);
    UserIngredientTracker DTOUserITResToUserIT(UserIngredientTrackerResponseDTO dtoUserIT);
}
