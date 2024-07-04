package fin.av.thesis.DTO.Response;

import fin.av.thesis.DAL.Enum.SupportedLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDTO {
    private String id;
    private String userId;
    private SupportedLanguage language;
}
