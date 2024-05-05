package fin.av.thesis.REST.Mapper.RequestMapper;

import fin.av.thesis.DAL.Document.Nutrition.UserHealthTracker;
import fin.av.thesis.DTO.Request.UserHealthTrackerRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserHealthTrackerRequestMapper {
    UserHealthTracker DTOUserHTReqToUserHT(UserHealthTrackerRequestDTO dtoUserHT);
    UserHealthTrackerRequestDTO UserHTToDTOUserHTReq(UserHealthTracker userHT);
}
