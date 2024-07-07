package fin.av.thesis.DTO.Request;

import fin.av.thesis.DAL.Enum.SupportedLanguage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserAndUPDTO(
        @NotBlank(message = "First name is required!") String firstName,
        @NotBlank(message = "Last name is required!") String lastName,
        @NotBlank(message = "Username is required!") String username,
        String password,
        boolean enabled,
        @NotBlank(message = "Email is required!")
        @Email(message = "Must be a valid email address!") String email,
        @NotBlank(message = "Phone number is required!") String phoneNumber,
        SupportedLanguage language
) {}
