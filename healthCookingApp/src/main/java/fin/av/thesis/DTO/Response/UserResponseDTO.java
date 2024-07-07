package fin.av.thesis.DTO.Response;

import java.util.Set;

public record UserResponseDTO(
        String id,
        String firstName,
        String lastName,
        String username,
        boolean enabled,
        String email,
        String phoneNumber,
        Set<String> authorities
) {}