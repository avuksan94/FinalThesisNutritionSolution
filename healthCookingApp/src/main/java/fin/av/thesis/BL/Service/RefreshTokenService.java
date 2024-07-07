package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.UserManagement.RefreshToken;
import reactor.core.publisher.Mono;

public interface RefreshTokenService {
    public Mono<RefreshToken> createRefreshToken(String userId);
    public Mono<RefreshToken> validateRefreshToken(String token);
    public Mono<Void> deleteRefreshTokenByUserId(String userId);
}
