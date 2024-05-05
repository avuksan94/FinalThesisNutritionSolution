package fin.av.thesis.REST.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fin.av.thesis.BL.Service.HealthWarningService;
import fin.av.thesis.BL.Service.OpenAIService;
import fin.av.thesis.BL.Service.UserHealthTrackerService;
import fin.av.thesis.DAL.Document.Nutrition.HealthWarning;
import fin.av.thesis.DAL.Document.Nutrition.UserHealthTracker;
import fin.av.thesis.DAL.Document.OpenAI.DietPrompt;
import fin.av.thesis.DAL.Document.OpenAI.HealthWarningResponse;
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

import java.util.List;

@RestController
@RequestMapping("healthAPI")
public class HealthTrackerController {
    private final OpenAIService openAIService;
    private final HealthWarningService healthWarningService;
    private final UserHealthTrackerService userHealthTrackerService;
    private final UserHealthTrackerRequestMapper userHealthTrackerRequestMapper;
    private final UserHealthTrackerResponseMapper userHealthTrackerResponseMapper;
    private final ObjectMapper objectMapper;

    public HealthTrackerController(OpenAIService openAIService, HealthWarningService healthWarningService, UserHealthTrackerService userHealthTrackerService, UserHealthTrackerRequestMapper userHealthTrackerRequestMapper, UserHealthTrackerResponseMapper userHealthTrackerResponseMapper, ObjectMapper objectMapper) {
        this.openAIService = openAIService;
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

    //TESTING THE CONCEPT
    @PostMapping("/healthTrackers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new health tracker", description = "Creates a new health tracker.")
    public Mono<ResponseEntity<?>> createUserHT(@Valid @RequestBody UserHealthTrackerRequestDTO userHt) {
        Mono<List<String>> healthConditions = HealthTrackingHelper.getHealthListMono(userHt);
        Mono<List<String>> knownAllergies = HealthTrackingHelper.getAllergyListMono(userHt);

        return Mono.zip(healthConditions, knownAllergies)
                .flatMap(tuple -> {
                    DietPrompt prompt = new DietPrompt(userHt.getDiet(), tuple.getT1(), tuple.getT2());
                    return openAIService.checkDietCompatibility(prompt);
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
                                            .map(savedTracker -> ResponseEntity.ok(savedTracker));
                                });
                    } catch (JsonProcessingException e) {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage()));
                    }
                });
    }

    @PutMapping("/healthTrackers/{healthTrackerId}")
    @Operation(summary = "Updates an health tracker based on the health tracker ID", description = "Updates an health tracker by the health tracker ID.")
    public Mono<ResponseEntity<?>> updateUser(@PathVariable String healthTrackerId, @Valid @RequestBody UserHealthTrackerRequestDTO userHt) {
        Mono<List<String>> healthConditions = HealthTrackingHelper.getHealthListMono(userHt);
        Mono<List<String>> knownAllergies = HealthTrackingHelper.getAllergyListMono(userHt);

        return Mono.zip(healthConditions, knownAllergies)
                .flatMap(tuple -> {
                    DietPrompt prompt = new DietPrompt(userHt.getDiet(), tuple.getT1(), tuple.getT2());
                    return openAIService.checkDietCompatibility(prompt);
                })
                .flatMap(response -> {
                    try {
                        String json = response.getChoices().getFirst().getMessage().getContent();
                        String processedJson = HealthTrackingHelper.convertFractionsToDecimal(json);
                        HealthWarningResponse healthWarningResponse = objectMapper.readValue(processedJson, HealthWarningResponse.class);

                        Mono<UserHealthTracker> existingTrackerMono = userHealthTrackerService.findById(healthTrackerId);

                        return existingTrackerMono.flatMap(existingTracker -> {
                            Mono<HealthWarning> existingHealthWarningMono = healthWarningService.findById(existingTracker.getHealthWarningId());

                            return existingHealthWarningMono.flatMap(existingHealthWarning -> {
                                HealthTrackingHelper.updateHealthWarningFromResponse(existingHealthWarning, healthWarningResponse);
                                return healthWarningService.save(existingHealthWarning)
                                        .flatMap(updatedHealthWarning -> {
                                            HealthTrackingHelper.updateUHTDetails(existingTracker, userHealthTrackerRequestMapper.DTOUserHTReqToUserHT(userHt));
                                            return userHealthTrackerService.save(existingTracker);
                                        })
                                        .thenReturn(ResponseEntity.ok(existingTracker));
                            });
                        });
                    } catch (JsonProcessingException e) {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage()));
                    }
                });
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
}
