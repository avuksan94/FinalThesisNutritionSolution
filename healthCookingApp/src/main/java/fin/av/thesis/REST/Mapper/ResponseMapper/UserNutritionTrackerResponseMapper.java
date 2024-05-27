package fin.av.thesis.REST.Mapper.ResponseMapper;

import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import fin.av.thesis.DTO.Response.UserNutritionTrackerResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserNutritionTrackerResponseMapper {
    UserNutritionTrackerResponseDTO UserNToDTOUserNTRes(UserNutritionTracker userNT);
    UserNutritionTracker DTOUserNTResToUserNT(UserNutritionTrackerResponseDTO dtoUserNT);
}
