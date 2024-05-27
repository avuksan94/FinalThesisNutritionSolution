package fin.av.thesis.REST.Mapper.RequestMapper;

import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import fin.av.thesis.DTO.Request.UserNutritionTrackerRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserNutritionTrackerRequestMapper {
    UserNutritionTracker DTOUserNTReqToUserNT(UserNutritionTrackerRequestDTO dtoUserNT);
    UserNutritionTrackerRequestDTO UserNTToDTOUserNTReq(UserNutritionTracker userNT);
}
