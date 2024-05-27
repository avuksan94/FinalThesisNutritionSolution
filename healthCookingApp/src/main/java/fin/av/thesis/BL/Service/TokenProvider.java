package fin.av.thesis.BL.Service;

import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface TokenProvider {
    public Mono<String> generateToken(UserDetails userDetails);
}
