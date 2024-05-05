package fin.av.thesis.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean enabled;
    private String email;
    private String phoneNumber;
    private Set<String> authorities;
}
