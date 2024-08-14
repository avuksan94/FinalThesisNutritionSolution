package fin.av.thesis.CONFIG;

import fin.av.thesis.BL.ServiceImpl.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


import java.util.Arrays;

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

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                     JwtAuthenticationManager authenticationManager,
                                                     ServerAuthenticationConverter authenticationConverter) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, "/healthAPI/authentication/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/healthAPI/authentication/register").permitAll()
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    public JwtAuthenticationManager jwtAuthenticationManager(JwtService jwtService) {
        return new JwtAuthenticationManager(jwtService);
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("https://finalthesisnutritionfrontend-production.up.railway.app"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
