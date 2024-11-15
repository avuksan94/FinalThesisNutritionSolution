package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DAL.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final CustomUserServiceImpl userService;

    public CustomUserDetailsService(CustomUserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.findByUsernameWithAuthorities(username)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User not found with username: " + username))))
                .map(user -> {
                    Set<GrantedAuthority> authorities = user.getAuthorities().stream()
                            .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().toString()))
                            .collect(Collectors.toSet());
                    logger.info("CustomUserDetails: AUTHORITIES: " + authorities);
                    logger.info("CustomUserDetails: PASSWORD" + user.getPassword());
                    return new org.springframework.security.core.userdetails.User(
                            user.getUsername(), user.getPassword(), authorities);
                });
    }
}