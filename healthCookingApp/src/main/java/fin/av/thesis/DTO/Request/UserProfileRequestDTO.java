package fin.av.thesis.DTO.Request;

import fin.av.thesis.DAL.Enum.SupportedLanguage;

public record UserProfileRequestDTO(
        SupportedLanguage language
) {}