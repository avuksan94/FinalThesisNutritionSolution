package fin.av.thesis.REST.Controller;

import fin.av.thesis.BL.Service.UserProfileService;
import fin.av.thesis.BL.Service.UserService;
import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import fin.av.thesis.DAL.Document.Nutrition.UserProfile;
import fin.av.thesis.DTO.Request.UserProfileRequestDTO;
import fin.av.thesis.DTO.Response.RecipeResponseDTO;
import fin.av.thesis.DTO.Response.UserNutritionTrackerResponseDTO;
import fin.av.thesis.DTO.Response.UserProfileResponseDTO;
import fin.av.thesis.REST.Mapper.RequestMapper.UserProfileRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.UserProfileResponseMapper;
import fin.av.thesis.UTIL.CustomAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("healthAPI")
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final UserService userService;
    private final UserProfileResponseMapper userProfileResponseMapper;

    public UserProfileController(UserProfileService userProfileService, UserService userService, UserProfileResponseMapper userProfileResponseMapper) {
        this.userProfileService = userProfileService;
        this.userService = userService;
        this.userProfileResponseMapper = userProfileResponseMapper;
    }


    @GetMapping("/userProfiles/all")
    @ResponseBody
    @Operation(summary = "Find all user profiles", description = "Returns all user profiles.")
    public Mono<ResponseEntity<List<UserProfileResponseDTO>>> findAllUserProfiles() {
        return userProfileService.findAll()
                .collectList()
                .map(userProfiles -> ResponseEntity.ok(userProfiles.stream()
                        .map(userProfileResponseMapper::UserProfileToDTOUserProfileRes)
                        .toList()));
    }

    @GetMapping("/userProfiles/{profileId}")
    @ResponseBody
    @Operation(summary = "Find user profile by ID", description = "Returns a specific user profile.")
    public Mono<ResponseEntity<UserProfileResponseDTO>> findProfileById(@PathVariable String profileId) {
        return userProfileService.findById(profileId)
                .map(userProfileResponseMapper::UserProfileToDTOUserProfileRes)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/userProfilesByUsername/{username}")
    @Operation(summary = "Find profile by username", description = "Returns a specific user profile.")
    public Mono<ResponseEntity<UserProfileResponseDTO>> findProfileByUsername(@PathVariable String username) {
        return userProfileService.findUserProfileByUsername(username)
                .map(userProfileResponseMapper::UserProfileToDTOUserProfileRes)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/userProfilesByUsername/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user profile", description = "Creates a new user profile.")
    public Mono<ResponseEntity<UserProfileResponseDTO>> createUserProfileByUsername(@PathVariable String username,
                                                                                   @Valid @RequestBody UserProfileRequestDTO userProfileRequestDTO) {
        return userService.findByUsername(username)
                .flatMap(user -> {
                    UserProfile userProfile = new UserProfile();
                    userProfile.setUserId(user.getId());
                    userProfile.setLanguage(userProfileRequestDTO.language());

                    return userProfileService.save(userProfile);
                })
                .map(userProfileResponseMapper::UserProfileToDTOUserProfileRes)
                .map(ResponseEntity::ok)
                .onErrorMap(CustomAlreadyExistsException.class, e -> new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    @ResponseBody
    @Operation(summary = "Delete a user profile", description = "Specify the profile ID, to delete the profile you want.")
    @DeleteMapping("/userProfiles/{profileId}")
    public Mono<ResponseEntity<String>> deleteUserProfile(@PathVariable String profileId) {
        return userProfileService.deleteById(profileId)
                .then(Mono.just(ResponseEntity.ok("Successfully deleted profile with ID " + profileId)));
    }
}
