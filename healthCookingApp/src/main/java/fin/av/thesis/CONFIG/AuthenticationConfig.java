package fin.av.thesis.CONFIG;

import fin.av.thesis.JWT.JwtRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/*
The UserDetails interface is the contract you use to describe a user in Spring
Security.
- The UserDetailsService interface is the contract that Spring Security
expects you to implement in the authentication architecture to describe the
way the application obtains user details.
- The UserDetailsManager interface extends the UserDetailsService and
adds the behavior related to creating, changing, or deleting a user.
- Spring Security provides a few implementations of the UserDetailsManager
contract. Among these are InMemoryUserDetailsManager, JdbcUser-
DetailsManager, and LdapUserDetailsManager.

Page 85 Spring Security
*/

@EnableWebFluxSecurity
@Configuration
public class AuthenticationConfig {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationConfig.class);
    private final JwtRequestFilter jwtRequestFilter;

    public AuthenticationConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        logger.info("Configuring Reactive Security Filter Chain...");
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, "/healthAPI/authentication/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/healthAPI/authentication/register").permitAll()
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(jwtRequestFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }
}
