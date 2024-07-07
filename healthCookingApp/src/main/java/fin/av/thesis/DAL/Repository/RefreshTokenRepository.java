package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.UserManagement.RefreshToken;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RefreshTokenRepository extends ReactiveMongoRepository<RefreshToken, String> {
    Mono<RefreshToken> findByToken(String token);
    Mono<Void> deleteByUserId(String userId);
}
