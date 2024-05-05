package fin.av.thesis.REST.Mapper.ResponseMapper;

import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import fin.av.thesis.DAL.Document.Nutrition.UserHealthTracker;
import fin.av.thesis.DTO.Response.RecipeResponseDTO;
import fin.av.thesis.DTO.Response.UserHealthTrackerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserHealthTrackerResponseMapper {
    UserHealthTrackerResponseDTO UserHToDTOUserHTRes(UserHealthTracker userHT);
    UserHealthTracker DTOUserHTResToUserHT(UserHealthTrackerResponseDTO dtoUserHT);
}
