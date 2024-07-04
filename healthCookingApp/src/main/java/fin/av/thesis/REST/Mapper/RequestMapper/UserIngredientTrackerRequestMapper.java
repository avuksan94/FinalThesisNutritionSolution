package fin.av.thesis.REST.Mapper.RequestMapper;

import fin.av.thesis.DAL.Document.Nutrition.UserIngredientTracker;
import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import fin.av.thesis.DTO.Request.UserIngredientTrackerRequestDTO;
import fin.av.thesis.DTO.Request.UserNutritionTrackerRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserIngredientTrackerRequestMapper {
    UserIngredientTracker DTOUserITReqToUserIT(UserIngredientTrackerRequestDTO dtoUserIT);
    UserIngredientTrackerRequestDTO UserITToDTOUserITReq(UserNutritionTracker userNT);
}
