package fin.av.thesis.DTO.Response;

import fin.av.thesis.DAL.Enum.SupportedLanguage;

public record UserProfileResponseDTO(
        String id,
        String userId,
        SupportedLanguage language
) {}
