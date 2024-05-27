package fin.av.thesis.CONFIG;

import fin.av.thesis.BL.ServiceImpl.JwtService;
import fin.av.thesis.JWT.JwtToken;
import fin.av.thesis.UTIL.CustomErrorResponse;
import fin.av.thesis.UTIL.CustomForbiddenException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationManager.class);

    private final JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        logger.info("AUTH MANAGER INFO:");
        logger.info(authentication.getAuthorities().toString());
        logger.info(authentication.getCredentials().toString());
        logger.info(authentication.getPrincipal().toString());

        return Mono.just(authentication)
                .cast(JwtToken.class)
                .flatMap(jwtToken -> jwtService.isTokenValid(jwtToken.getToken())
                        .filter(Boolean::booleanValue)
                        .map(valid -> jwtToken.withAuthenticated(true))
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new CustomForbiddenException("Invalid token.")))))
                .cast(Authentication.class);
    }
}