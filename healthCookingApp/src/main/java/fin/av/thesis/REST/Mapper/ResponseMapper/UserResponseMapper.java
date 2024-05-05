package fin.av.thesis.REST.Mapper.ResponseMapper;

import fin.av.thesis.DAL.Document.UserManagement.Authority;
import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DTO.Response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    @Mapping(target = "authorities", source = "authorities", qualifiedByName = "authoritiesToStringSet")
    UserResponseDTO UserToDTOUserRes(User user);

    @Named("authoritiesToStringSet")
    default Set<String> authoritiesToStringSet(Set<Authority> authorities) {
        if (authorities == null) {
            return Collections.emptySet();
        }
        return authorities.stream()
                .map(authority -> authority.getAuthority().name())
                .collect(Collectors.toSet());
    }
}
