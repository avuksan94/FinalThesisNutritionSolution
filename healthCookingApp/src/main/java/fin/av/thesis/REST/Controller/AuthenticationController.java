package fin.av.thesis.REST.Controller;


import fin.av.thesis.BL.Service.TokenProvider;
import fin.av.thesis.DAL.Document.UserManagement.Authority;
import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DAL.Enum.Role;
import fin.av.thesis.DTO.Request.UserRequestDTO;
import fin.av.thesis.REST.Mapper.RequestMapper.UserRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.UserResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import fin.av.thesis.BL.Service.AuthorityService;
import fin.av.thesis.BL.Service.EncryptionService;
import fin.av.thesis.BL.Service.UserService;
import fin.av.thesis.JWT.JwtResponse;
import fin.av.thesis.JWT.LoginRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("healthAPI")
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;
    private final AuthorityService authorityService;
    private final ReactiveUserDetailsService userDetailsService;
    private final EncryptionService encryptionService;
    private final ReactiveAuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public AuthenticationController(UserService userService, UserRequestMapper userRequestMapper, UserResponseMapper userResponseMapper, AuthorityService authorityService, ReactiveUserDetailsService userDetailsService, EncryptionService encryptionService, ReactiveAuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.userService = userService;
        this.userRequestMapper = userRequestMapper;
        this.userResponseMapper = userResponseMapper;
        this.authorityService = authorityService;
        this.userDetailsService = userDetailsService;
        this.encryptionService = encryptionService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }


    @PostMapping("/authentication/login")
    public Mono<ResponseEntity<?>> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) {
        logger.debug("Login request: {}", authenticationRequest);
        return authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword())
                .flatMap(authentication -> userDetailsService.findByUsername(authenticationRequest.getUsername()))
                .flatMap(userDetails -> tokenProvider.generateToken(userDetails)
                        .map(token -> {
                            logger.info("Created token: {} for user: {}", token, authenticationRequest.getUsername());
                            return ResponseEntity.ok(new JwtResponse(token));
                        })
                );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/authentication/register")
    @Operation(summary = "Creates a new User entity", description = "Creates and return a new User entity.")
    public Mono<ResponseEntity<?>> registerUserAsBasicUser(@Valid @RequestBody UserRequestDTO userRequest) {
        return createUserAndAssignRole(userRequest, Role.ROLE_USER);
    }

    private Mono<Authentication> authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return this.authenticationManager.authenticate(authToken);
    }

    private Mono<ResponseEntity<?>> createUserAndAssignRole(UserRequestDTO userRequestDTO, Role role) {
        return Mono.fromCallable(() -> encryptionService.doBCryptPassEncoding(userRequestDTO.getPassword()))
                .doOnSuccess(encryptedPassword -> {
                    logger.info("Encoded password: {}", encryptedPassword);
                    userRequestDTO.setPassword(encryptedPassword);
                })
                .flatMap(encryptedPassword -> {
                    User user = userRequestMapper.DTOUserReqToUser(userRequestDTO);
                    user.setPassword(encryptedPassword);
                    return userService.save(user);
                })
                .flatMap(savedUser -> {
                    logger.info("User saved with encrypted password: {}", savedUser.getPassword());
                    Authority authority = new Authority(savedUser.getId(), role);
                    return authorityService.save(authority)
                            .thenReturn(savedUser);
                })
                .flatMap(savedUser -> userService.findByIdWithAuthorities(savedUser.getId()))
                .map(userResponseMapper::UserToDTOUserRes)
                .map(ResponseEntity::ok);
    }
}
