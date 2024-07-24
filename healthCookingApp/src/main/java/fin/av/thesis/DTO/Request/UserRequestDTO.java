package fin.av.thesis.DTO.Request;

import jakarta.validation.constraints.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank(message = "First name is required!")
        @Size(max = 50, message = "First name must be at most 50 characters") String firstName,
        @NotBlank(message = "Last name is required!")
        @Size(max = 50, message = "Last name must be at most 50 characters") String lastName,
        String username,
        String password,
        boolean enabled,
        @Email(message = "Must be a valid email address!") String email,
        @NotBlank(message = "Phone number is required!") String phoneNumber
) {}

