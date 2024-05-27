package fin.av.thesis.REST.Controller;

import fin.av.thesis.BL.Service.AuthorityService;
import fin.av.thesis.BL.Service.UserService;
import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import fin.av.thesis.DAL.Document.UserManagement.Authority;
import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DAL.Enum.Role;
import fin.av.thesis.DTO.Request.IngredientRequestDTO;
import fin.av.thesis.DTO.Request.RecipeRequestDTO;
import fin.av.thesis.DTO.Request.UserRequestDTO;
import fin.av.thesis.DTO.Response.IngredientResponseDTO;
import fin.av.thesis.DTO.Response.RecipeResponseDTO;
import fin.av.thesis.DTO.Response.UserResponseDTO;
import fin.av.thesis.REST.Helper.UserHelper;
import fin.av.thesis.REST.Mapper.RequestMapper.UserRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.UserResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("healthAPI")
public class UserController {
    private final UserService userService;
    private final AuthorityService authorityService;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;

    public UserController(UserService userService, AuthorityService authorityService, UserRequestMapper userRequestMapper, UserResponseMapper userResponseMapper) {
        this.userService = userService;
        this.authorityService = authorityService;
        this.userRequestMapper = userRequestMapper;
        this.userResponseMapper = userResponseMapper;
    }

    @GetMapping("/users/all")
    @Operation(summary = "Find all users", description = "Returns all users.")
    public Mono<ResponseEntity<List<UserResponseDTO>>> findAllUsers() {
        return userService.findAll()
                .flatMap(user ->
                        authorityService.findByUserId(user.getId())
                                .collectList()
                                .map(authorities -> {
                                    user.setAuthorities(new HashSet<>(authorities));
                                    return user;
                                })
                )
                .collectList()
                .map(users -> ResponseEntity.ok(users.stream()
                        .map(userResponseMapper::UserToDTOUserRes)
                        .toList()));
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Find user by ID", description = "Returns a specific user.")
    public Mono<ResponseEntity<UserResponseDTO>> findUserById(@PathVariable String userId) {
        return userService.findByIdWithAuthorities(userId)
                .map(userResponseMapper::UserToDTOUserRes)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/usersBasic/{username}")
    @Operation(summary = "Find user by Username", description = "Returns a specific user.")
    public Mono<ResponseEntity<UserResponseDTO>> findUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(userResponseMapper::UserToDTOUserRes)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user", description = "Creates a new user and returns the created user.")
    public Mono<ResponseEntity<UserResponseDTO>> createBasicUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return createUserAndAssignRole(userRequestDTO, Role.ROLE_USER);
    }

    @PostMapping("/admins")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new admin user", description = "Creates a new admin user and returns the created user.")
    public Mono<ResponseEntity<UserResponseDTO>> createAdminUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return createUserAndAssignRole(userRequestDTO, Role.ROLE_ADMIN);
    }

    private Mono<ResponseEntity<UserResponseDTO>> createUserAndAssignRole(UserRequestDTO userRequestDTO, Role role) {
        return userService.save(userRequestMapper.DTOUserReqToUser(userRequestDTO))
                .flatMap(savedUser -> {
                    Authority authority = new Authority(savedUser.getId(), role);
                    return authorityService.save(authority)
                            .then(Mono.just(savedUser));
                })
                .flatMap(savedUser -> userService.findByIdWithAuthorities(savedUser.getId()))
                .map(userResponseMapper::UserToDTOUserRes)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/users/{userId}")
    @Operation(summary = "Updates an user based on the userId", description = "Updates a user.")
    public Mono<ResponseEntity<UserResponseDTO>> updateUser(@PathVariable String userId, @Valid @RequestBody UserRequestDTO userDTO) {
        return userService.findById(userId)
                .flatMap(existingUser -> {
                    User updatedUser = userRequestMapper.DTOUserReqToUser(userDTO);
                    UserHelper.updateUser(existingUser, updatedUser);
                    return userService.save(existingUser);
                })
                .flatMap(savedUser -> userService.findByIdWithAuthorities(savedUser.getId()))
                .map(userResponseMapper::UserToDTOUserRes)
                .map(ResponseEntity::ok);
    }

    @ResponseBody
    @Operation(summary = "Delete a user", description = "Specify the user ID, to delete the user you want.")
    @DeleteMapping("/users/{userId}")
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable String userId) {
        return authorityService.deleteByUserId(userId)
                .then(userService.deleteById(userId))
                .then(Mono.just(ResponseEntity.ok("Successfully deleted recipe with ID " + userId)));
    }
}
