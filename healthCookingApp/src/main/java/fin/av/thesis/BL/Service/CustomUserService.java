package fin.av.thesis.BL.Service;

import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface CustomUserService {
    Mono<UserDetails> findByUsernameWithAuthorities(String username);
}
