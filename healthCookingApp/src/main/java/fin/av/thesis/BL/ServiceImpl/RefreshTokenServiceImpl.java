package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.RefreshTokenService;
import fin.av.thesis.DAL.Document.UserManagement.RefreshToken;
import fin.av.thesis.DAL.Repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Mono<RefreshToken> createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(604800000));
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Mono<RefreshToken> validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(refreshToken -> refreshToken.getExpiryDate().isAfter(Instant.now()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid or expired refresh token")));
    }

    @Override
    public Mono<Void> deleteRefreshTokenByUserId(String userId) {
        return refreshTokenRepository.deleteByUserId(userId);
    }
}
