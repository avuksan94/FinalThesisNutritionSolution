package fin.av.thesis.REST.Mapper.RequestMapper;

import fin.av.thesis.DAL.Document.Nutrition.UserProfile;
import fin.av.thesis.DTO.Request.UserProfileRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileRequestMapper {
    UserProfile DTOUserProfileReqToUserProfile(UserProfileRequestDTO userProfileRequestDTO);
    UserProfileRequestDTO UserProfileToDTOUserProfileReq(UserProfile userProfile);
}
