package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.TokenProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService implements TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public Mono<String> extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Mono<Date> extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> Mono<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
        return extractAllClaims(token).map(claimsResolver);
    }

    private Mono<Claims> extractAllClaims(String token) {
        return Mono.fromCallable(() -> Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody())
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(claims -> logger.info("EXTRACTING CLAIMS"))
                .doOnError(e -> logger.error("Error extracting claims: ", e));
    }

    public Mono<Boolean> isTokenExpired(String token) {
        return extractExpiration(token).map(expiration -> expiration.before(new Date()));
    }

    public Mono<Boolean> isTokenValid(String jwt) {
        return isTokenExpired(jwt)
                .map(expired -> !expired);
    }

    @Override
    public Mono<String> generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return Mono.fromCallable(() -> createToken(claims, userDetails.getUsername()))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(token -> logger.info("TOKEN GENERATED"))
                .doOnError(e -> logger.error("Error generating token: ", e));
    }

    @Override
    public Mono<String> generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        return Mono.fromCallable(() -> createRefreshToken(claims))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private String createRefreshToken(Map<String, Object> claims) {
        long expirationTimeLong = 1000 * 60 * 60 * 24 * 14; // 14 days
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeLong))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    @Override
    public Mono<Boolean> validateRefreshToken(String token) {
        return Mono.fromCallable(() -> !isTokenExpired(token).block())
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<List<SimpleGrantedAuthority>> extractRoles(String token) {
        return extractAllClaims(token).map(claims -> {
            List<String> roles = claims.get("roles", List.class);
            logger.info("TOKEN EXTRACTED");
            return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        });
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}