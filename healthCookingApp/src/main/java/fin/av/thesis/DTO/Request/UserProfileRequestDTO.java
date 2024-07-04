package fin.av.thesis.DTO.Request;

import fin.av.thesis.DAL.Enum.SupportedLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequestDTO {
    private SupportedLanguage language;
}
