package fin.av.thesis.CONFIG;

import fin.av.thesis.BL.ServiceImpl.JwtService;
import fin.av.thesis.JWT.JwtToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtService jwtService;
    private static final String BEARER = "Bearer ";
    private static final Logger logger = LoggerFactory.getLogger(JwtServerAuthenticationConverter.class);

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        logger.info("Attempting to extract authorization header from exchange.");
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(header -> {
                    boolean startsWithBearer = header.startsWith(BEARER);
                    if (!startsWithBearer) {
                        logger.info("Authorization header does not start with Bearer.");
                    }
                    return startsWithBearer;
                })
                .map(header -> {
                    logger.info("Stripping Bearer prefix from authorization header.");
                    return header.substring(BEARER.length());
                })
                .flatMap(this::createJwtAuthentication);
    }

    private Mono<Authentication> createJwtAuthentication(String token) {
        logger.info("Creating JWT authentication for token: {}", token);
        return createUserDetails(token)
                .map(userDetails -> {
                    logger.debug("Creating JwtToken with username: {}", userDetails.getUsername());
                    return new JwtToken(token, userDetails);
                });
    }

    private Mono<UserDetails> createUserDetails(String token) {
        logger.info("Extracting user details using token.");
        return jwtService.extractUsername(token)
                .flatMap(username -> createAuthorities(token)
                        .collectList()
                        .map(authorities -> {
                            logger.info("Creating user details for username: {}, with authorities: {}", username, authorities);
                            return User.builder()
                                    .username(username)
                                    .authorities(authorities)
                                    .password("")
                                    .build();
                        }));
    }

    private Flux<SimpleGrantedAuthority> createAuthorities(String token) {
        logger.info("Extracting roles from token.");
        return jwtService.extractRoles(token)
                .flatMapMany(Flux::fromIterable)
                .map(role -> {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
                    logger.info("Granted authority created: {}", authority.getAuthority());
                    return authority;
                });
    }

}