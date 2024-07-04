package fin.av.thesis.DTO.Request;

import fin.av.thesis.DAL.Enum.SupportedLanguage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAndUPDTO {
    @NotBlank(message = "First name is required!")
    private String firstName;

    @NotBlank(message = "Last name is required!")
    private String lastName;

    @NotBlank(message = "Username is required!")
    private String username;

    private String password;
    private boolean enabled;
    @NotBlank(message = "Email is required!")
    @Email(message = "Must be a valid email address!")
    private String email;
    @NotBlank(message = "Phone number is required!")
    private String phoneNumber;
    private SupportedLanguage language;
}
