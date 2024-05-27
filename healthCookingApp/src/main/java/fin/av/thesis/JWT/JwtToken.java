package fin.av.thesis.JWT;

import fin.av.thesis.CONFIG.AuthenticationConfig;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class JwtToken implements Authentication {
    private static final Logger logger = LoggerFactory.getLogger(JwtToken.class);

    private final String token;
    private final UserDetails principal;
    private volatile boolean authenticated = false;

    public JwtToken(String token, UserDetails userDetails) {
        this.token = token;
        this.principal = userDetails;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return principal.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return token;
    }


    @Override
    public Object getDetails() {
        return null;  // Optionally include more details if necessary
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal.getUsername();
    }

    public JwtToken withAuthenticated(boolean isAuthenticated) {
        setAuthenticated(isAuthenticated);  // Use the setter to ensure any associated logic is handled
        return this;
    }
}