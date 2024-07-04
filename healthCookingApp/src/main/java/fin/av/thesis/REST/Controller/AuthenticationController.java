package fin.av.thesis.REST.Controller;


import fin.av.thesis.BL.Service.*;
import fin.av.thesis.DAL.Document.Nutrition.UserProfile;
import fin.av.thesis.DAL.Document.UserManagement.Authority;
import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DAL.Enum.Role;
import fin.av.thesis.DTO.Request.UserAndUPDTO;
import fin.av.thesis.DTO.Request.UserProfileRequestDTO;
import fin.av.thesis.DTO.Request.UserRequestDTO;
import fin.av.thesis.DTO.Response.UserProfileResponseDTO;
import fin.av.thesis.DTO.Response.UserResponseDTO;
import fin.av.thesis.REST.Mapper.RequestMapper.UserProfileRequestMapper;
import fin.av.thesis.REST.Mapper.RequestMapper.UserRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.UserProfileResponseMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.UserResponseMapper;
import fin.av.thesis.UTIL.CustomAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("healthAPI")
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;
    private final UserProfileService userProfileService;
    private final AuthorityService authorityService;
    private final ReactiveUserDetailsService userDetailsService;
    private final EncryptionService encryptionService;
    private final ReactiveAuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public AuthenticationController(UserService userService, UserRequestMapper userRequestMapper, UserResponseMapper userResponseMapper, UserProfileService userProfileService, AuthorityService authorityService, ReactiveUserDetailsService userDetailsService, EncryptionService encryptionService, ReactiveAuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.userService = userService;
        this.userRequestMapper = userRequestMapper;
        this.userResponseMapper = userResponseMapper;
        this.userProfileService = userProfileService;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/authentication/register")
    @Operation(summary = "Creates a new User entity(with language support)", description = "Creates and return a new User entity.")
    public Mono<ResponseEntity<?>> registerUserAsBasicUser(@Valid @RequestBody UserAndUPDTO userRequest) {
        UserRequestDTO user = mapSpecialToUserReqDTO(userRequest);
        UserProfileRequestDTO userProfile = new UserProfileRequestDTO();
        userProfile.setLanguage(userRequest.getLanguage());

        return createUserAndAssignRole(user, Role.ROLE_USER)
                .flatMap(responseEntity -> {
                    UserResponseDTO userResponse = (UserResponseDTO) responseEntity.getBody();
                    if (userResponse != null) {
                        return createUserProfile(userResponse.getUsername(), userProfile)
                                .thenReturn(responseEntity);
                    } else {
                        return Mono.error(new RuntimeException("User creation failed"));
                    }
                });
    }

    private Mono<UserProfile> createUserProfile(String username, UserProfileRequestDTO userProfileRequestDTO) {
        return userService.findByUsername(username)
                .flatMap(user -> {
                    UserProfile userProfile = new UserProfile();
                    userProfile.setUserId(user.getId());
                    userProfile.setLanguage(userProfileRequestDTO.getLanguage());

                    return userProfileService.save(userProfile);
                });
    }

    private static UserRequestDTO mapSpecialToUserReqDTO(UserAndUPDTO userRequest) {
        UserRequestDTO user = new UserRequestDTO();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setEnabled(userRequest.isEnabled());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        return user;
    }
}
