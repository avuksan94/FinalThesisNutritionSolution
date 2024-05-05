package fin.av.thesis.JWT;

import fin.av.thesis.BL.ServiceImpl.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtRequestFilter implements WebFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            return Mono.just(jwt)
                    .map(jwtUtil::extractUsername)
                    .flatMap(username -> userDetailsService.findByUsername(username)
                            .filter(userDetails -> jwtUtil.validateToken(jwt, userDetails))
                            .flatMap(userDetails -> setSecurityContextAndContinue(exchange, chain, userDetails, jwt))
                    )
                    .switchIfEmpty(chain.filter(exchange));
        }
        return chain.filter(exchange);
    }

    private Mono<Void> setSecurityContextAndContinue(ServerWebExchange exchange, WebFilterChain chain, UserDetails userDetails, String jwt) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext))
                .get(chain.filter(exchange));
    }
}