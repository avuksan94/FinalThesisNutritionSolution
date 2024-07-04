package fin.av.thesis.REST.Mapper.ResponseMapper;

import fin.av.thesis.DAL.Document.Nutrition.UserProfile;
import fin.av.thesis.DTO.Response.UserProfileResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileResponseMapper {
    UserProfileResponseDTO UserProfileToDTOUserProfileRes(UserProfile userProfile);
    UserProfile DTOUserProfileResToUserProfile(UserProfileResponseDTO userProfileResponseDTO);
}
