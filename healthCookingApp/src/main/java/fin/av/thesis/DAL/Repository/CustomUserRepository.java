package fin.av.thesis.DAL.Repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomUserRepository {
    Mono<UserDetails> findByUsernameWithAuthorities(String username);
}