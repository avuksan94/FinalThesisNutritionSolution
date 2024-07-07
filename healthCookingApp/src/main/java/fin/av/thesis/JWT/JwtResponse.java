package fin.av.thesis.JWT;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class JwtResponse {
    private String token;
    private String refreshToken;

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }
}
