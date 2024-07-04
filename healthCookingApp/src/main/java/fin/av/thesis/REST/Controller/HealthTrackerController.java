package fin.av.thesis.REST.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fin.av.thesis.BL.Service.*;
import fin.av.thesis.DAL.Document.Nutrition.HealthWarning;
import fin.av.thesis.DAL.Document.Nutrition.UserHealthTracker;
import fin.av.thesis.DAL.Document.Nutrition.UserProfile;
import fin.av.thesis.DAL.Document.OpenAI.DietPrompt;
import fin.av.thesis.DAL.Document.OpenAI.HealthWarningResponse;
import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DAL.Enum.SupportedLanguage;
import fin.av.thesis.DTO.Request.UserHealthTrackerRequestDTO;
import fin.av.thesis.DTO.Response.UserHealthTrackerResponseDTO;
import fin.av.thesis.REST.Mapper.RequestMapper.UserHealthTrackerRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.UserHealthTrackerResponseMapper;
import fin.av.thesis.REST.Helper.HealthTrackingHelper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("healthAPI")
public class HealthTrackerController {
    private final OpenAIService openAIService;
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final HealthWarningService healthWarningService;
    private final UserHealthTrackerService userHealthTrackerService;
    private final UserHealthTrackerRequestMapper userHealthTrackerRequestMapper;
    private final UserHealthTrackerResponseMapper userHealthTrackerResponseMapper;
    private final ObjectMapper objectMapper;

    public HealthTrackerController(OpenAIService openAIService, UserService userService, UserProfileService userProfileService, HealthWarningService healthWarningService, UserHealthTrackerService userHealthTrackerService, UserHealthTrackerRequestMapper userHealthTrackerRequestMapper, UserHealthTrackerResponseMapper userHealthTrackerResponseMapper, ObjectMapper objectMapper) {
        this.openAIService = openAIService;
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.healthWarningService = healthWarningService;
        this.userHealthTrackerService = userHealthTrackerService;
        this.userHealthTrackerRequestMapper = userHealthTrackerRequestMapper;
        this.userHealthTrackerResponseMapper = userHealthTrackerResponseMapper;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/healthTrackers/all")
    @Operation(summary = "Find all user health tracking info", description = "Returns all user health tracking info.")
    public Mono<ResponseEntity<List<UserHealthTrackerResponseDTO>>> findAllUserHTs() {
        return userHealthTrackerService.findAll()
                .collectList()
                .map(userHTs -> ResponseEntity.ok(userHTs.stream()
                        .map(userHealthTrackerResponseMapper::UserHToDTOUserHTRes)
                        .toList()));
    }

    @GetMapping("/healthTrackers/{healthTrackerId}")
    @Operation(summary = "Find health tracker by ID", description = "Returns a specific health tracker.")
    public Mono<ResponseEntity<UserHealthTrackerResponseDTO>> findUserHTById(@PathVariable String healthTrackerId) {
        return userHealthTrackerService.findById(healthTrackerId)
                .map(userHealthTrackerResponseMapper::UserHToDTOUserHTRes)
                .map(ResponseEntity::ok);
    }

    //bool if exists
    @GetMapping("/healthTrackersCheck/{username}")
    @Operation(summary = "Check if health tracker exists", description = "Returns a boolean indicating existence.")
    public Mono<ResponseEntity<Map<String, Boolean>>> checkIfExists(@PathVariable String username) {
        return userHealthTrackerService.healthTrackerExistsByUsername(username)
                .map(exists -> ResponseEntity.ok(Collections.singletonMap("exists", exists)));
    }


    @GetMapping("/healthTrackersBasic/{userId}")
    @Operation(summary = "Find health tracker by user ID", description = "Returns a specific health tracker.")
    public Mono<ResponseEntity<UserHealthTrackerResponseDTO>> findUserHTByUserId(@PathVariable String userId) {
        return userHealthTrackerService.findByUserId(userId)
                .map(userHealthTrackerResponseMapper::UserHToDTOUserHTRes)
                .map(ResponseEntity::ok);
    }

    //HEALTH WARNINGS
    @GetMapping("/healthWarning/{username}")
    @Operation(summary = "Find health warning by user ID", description = "Returns a specific health warning.")
    public Mono<HealthWarning> getHealthWarningByUsername(@PathVariable String username) {
        return userHealthTrackerService.findHealthWarningByUsername(username);
    }

    //BY USERNAME:
    @PostMapping("/healthTrackersByUsername/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new health tracker", description = "Creates a new health tracker for a specified user.")
    public Mono<ResponseEntity<?>> createUserHTByUsername(@Valid @RequestBody UserHealthTrackerRequestDTO userHt, @PathVariable String username) {
        return userService.findByUsername(username)
                .flatMap(user -> {
                    userHt.setUserId(user.getId());
                    Mono<List<String>> healthConditions = HealthTrackingHelper.getHealthListMono(userHt);
                    Mono<List<String>> knownAllergies = HealthTrackingHelper.getAllergyListMono(userHt);
                    Mono<SupportedLanguage> lang = getUserLanguage(username);

                    return Mono.zip(healthConditions, knownAllergies, lang)
                            .flatMap(tuple -> {
                                DietPrompt prompt = new DietPrompt(userHt.getDiet(), tuple.getT1(), tuple.getT2());
                                return openAIService.checkDietCompatibility(prompt, tuple.getT3());
                            })
                            .flatMap(response -> {
                                try {
                                    String json = response.getChoices().getFirst().getMessage().getContent();
                                    String processedJson = HealthTrackingHelper.convertFractionsToDecimal(json);
                                    HealthWarningResponse healthWarningResponse = objectMapper.readValue(processedJson, HealthWarningResponse.class);
                                    HealthWarning newWarning = HealthTrackingHelper.getHealthWarning(healthWarningResponse);

                                    return healthWarningService.save(newWarning)
                                            .flatMap(savedWarning -> {
                                                UserHealthTracker newTracker = userHealthTrackerRequestMapper.DTOUserHTReqToUserHT(userHt);
                                                newTracker.setHealthWarningId(savedWarning.getId());

                                                return userHealthTrackerService.save(newTracker)
                                                        .map(savedTracker -> ResponseEntity.ok().body(savedTracker));
                                            });
                                } catch (JsonProcessingException e) {
                                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage()));
                                }
                            });
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PutMapping("/healthTrackersByUsername/{username}")
    @Operation(summary = "Updates a health tracker based on a username", description = "Updates a health tracker by the username.")
    public Mono<ResponseEntity<?>> updateUserHealthTrackerByUsername(@PathVariable String username, @Valid @RequestBody UserHealthTrackerRequestDTO userHt) {
        Mono<List<String>> healthConditions = HealthTrackingHelper.getHealthListMono(userHt);
        Mono<List<String>> knownAllergies = HealthTrackingHelper.getAllergyListMono(userHt);
        Mono<SupportedLanguage> lang = getUserLanguage(username);

        return Mono.zip(healthConditions, knownAllergies, lang)
                .flatMap(tuple -> {
                    DietPrompt prompt = new DietPrompt(userHt.getDiet(), tuple.getT1(), tuple.getT2());
                    return openAIService.checkDietCompatibility(prompt, tuple.getT3());
                })
                .flatMap(response -> {
                    try {
                        String json = response.getChoices().getFirst().getMessage().getContent();
                        String processedJson = HealthTrackingHelper.convertFractionsToDecimal(json);
                        HealthWarningResponse healthWarningResponse = objectMapper.readValue(processedJson, HealthWarningResponse.class);

                        return userHealthTrackerService.findHealthTrackerByUsername(username)
                                .flatMap(existingTracker -> {
                                    if (existingTracker.getHealthWarningId() != null) {
                                        return healthWarningService.findById(existingTracker.getHealthWarningId())
                                                .flatMap(existingHealthWarning -> {
                                                    HealthTrackingHelper.updateHealthWarningFromResponse(existingHealthWarning, healthWarningResponse);
                                                    return healthWarningService.save(existingHealthWarning);
                                                })
                                                .flatMap(updatedHealthWarning -> {
                                                    HealthTrackingHelper.updateUHTDetails(existingTracker, userHealthTrackerRequestMapper.DTOUserHTReqToUserHT(userHt));
                                                    existingTracker.setHealthWarningId(updatedHealthWarning.getId());
                                                    return userHealthTrackerService.save(existingTracker);
                                                })
                                                .map(savedTracker -> ResponseEntity.ok().body(savedTracker));
                                    } else {
                                        return Mono.just(ResponseEntity.notFound().build());
                                    }
                                });
                    } catch (JsonProcessingException e) {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage()));
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @ResponseBody
    @Operation(summary = "Delete a health tracker", description = "Specify the health tracker ID, to delete the health tracker you want.")
    @DeleteMapping("/healthTrackers/{healthTrackerId}")
    public Mono<ResponseEntity<String>> deleteUserHT(@PathVariable String healthTrackerId) {
        return userHealthTrackerService.findById(healthTrackerId)
                .flatMap(tracker ->
                        healthWarningService.deleteById(tracker.getHealthWarningId())
                                .then(
                                        userHealthTrackerService.deleteById(healthTrackerId))
                )
                .thenReturn(ResponseEntity.ok("Successfully deleted health tracker with ID " + healthTrackerId))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    private Mono<SupportedLanguage> getUserLanguage(String username) {
        return userProfileService.findUserProfileByUsername(username)
                .map(UserProfile::getLanguage)
                .switchIfEmpty(Mono.just(SupportedLanguage.EN));
    }
}
