package fin.av.thesis.DTO.Request;

import jakarta.validation.constraints.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank(message = "First name is required!")
        @Size(max = 50, message = "First name must be at most 50 characters") String firstName,
        @NotBlank(message = "Last name is required!")
        @Size(max = 50, message = "Last name must be at most 50 characters") String lastName,
        @NotBlank(message = "Username is required!") String username,
        @NotBlank(message = "Passoword is required!")String password,
        boolean enabled,
        @NotBlank(message = "Email is required!")
        @Email(message = "Must be a valid email address!") String email,
        @NotBlank(message = "Phone number is required!") String phoneNumber
) {}

