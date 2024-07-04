package fin.av.thesis.REST.Controller;

import fin.av.thesis.BL.Service.UserIngredientTrackerService;
import fin.av.thesis.BL.Service.UserService;
import fin.av.thesis.DAL.Document.Nutrition.Ingredient;
import fin.av.thesis.DAL.Document.Nutrition.UserIngredientTracker;
import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DTO.Request.IngredientRequestDTO;
import fin.av.thesis.DTO.Request.UserIngredientTrackerRequestDTO;
import fin.av.thesis.DTO.Response.IngredientResponseDTO;
import fin.av.thesis.DTO.Response.UserIngredientTrackerResponseDTO;
import fin.av.thesis.DTO.Response.UserNutritionTrackerResponseDTO;
import fin.av.thesis.REST.Helper.IngredientHelper;
import fin.av.thesis.REST.Helper.UserIngredientTrackerHelper;
import fin.av.thesis.REST.Mapper.RequestMapper.UserIngredientTrackerRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.UserIngredientTrackerResponseMapper;
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
public class IngredientTrackerController {
    private final UserIngredientTrackerService userIngredientTrackerService;
    private final UserIngredientTrackerRequestMapper userIngredientTrackerRequestMapper;
    private final UserIngredientTrackerResponseMapper userIngredientTrackerResponseMapper;
    private final UserService userService;

    public IngredientTrackerController(UserIngredientTrackerService userIngredientTrackerService, UserIngredientTrackerRequestMapper userIngredientTrackerRequestMapper, UserIngredientTrackerResponseMapper userIngredientTrackerResponseMapper, UserService userService) {
        this.userIngredientTrackerService = userIngredientTrackerService;
        this.userIngredientTrackerRequestMapper = userIngredientTrackerRequestMapper;
        this.userIngredientTrackerResponseMapper = userIngredientTrackerResponseMapper;
        this.userService = userService;
    }

    @GetMapping("/ingredientTrackers/all")
    @Operation(summary = "Find all ingredient trackers", description = "Returns all ingredient trackers.")
    public Mono<ResponseEntity<List<UserIngredientTrackerResponseDTO>>> findAllIngredientTrackers() {
        return userIngredientTrackerService.findAll()
                .collectList()
                .map(ingredientTrackers -> ResponseEntity.ok(ingredientTrackers.stream()
                        .map(userIngredientTrackerResponseMapper::UserIToDTOUserITRes)
                        .toList()));
    }

    @GetMapping("/ingredientTrackersByUsername/{username}")
    @Operation(summary = "Find all ingredient trackers by username", description = "Returns all ingredient trackers by username.")
    public Mono<ResponseEntity<List<UserIngredientTrackerResponseDTO>>> findAllIngredientTrackersByUsername(@PathVariable String username) {
        return userIngredientTrackerService.findAllByUserName(username)
                .collectList()
                .map(ingredientTrackers -> ResponseEntity.ok(ingredientTrackers.stream()
                        .map(userIngredientTrackerResponseMapper::UserIToDTOUserITRes)
                        .toList()));
    }

    @PostMapping("/ingredientTrackerByUsername/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new ingredient trackers for user.", description = "Creates a new ingredient tracker.")
    public Mono<ResponseEntity<UserIngredientTrackerResponseDTO>> createIT(@PathVariable String username, @RequestBody UserIngredientTrackerRequestDTO ingredientTracker) {
        return userService.findByUsername(username)
                .flatMap(user -> {
                    UserIngredientTracker userIngredientTracker = userIngredientTrackerRequestMapper.DTOUserITReqToUserIT(ingredientTracker);
                    userIngredientTracker.setUserId(user.getId());
                    return userIngredientTrackerService.save(userIngredientTracker);
                })
                .map(userIngredientTrackerResponseMapper::UserIToDTOUserITRes)
                .map(ResponseEntity::ok)
                .onErrorMap(CustomAlreadyExistsException.class, e -> new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    @PutMapping("/ingredientTrackers/{ingredientTrackerId}")
    @Operation(summary = "Updates an ingredient tracker based on the ingredientTrackerId", description = "Updates an ingredient tracker.")
    public Mono<ResponseEntity<UserIngredientTrackerResponseDTO>> updateIngredientTracker(@PathVariable String ingredientTrackerId, @Valid @RequestBody UserIngredientTrackerRequestDTO ingredientTracker) {
        return userIngredientTrackerService.findById(ingredientTrackerId)
                .flatMap(existingIngredientTracker -> {
                    UserIngredientTracker updatedIngredientTracker = userIngredientTrackerRequestMapper.DTOUserITReqToUserIT(ingredientTracker);
                    UserIngredientTrackerHelper.updateIngredientTrackerDetails(existingIngredientTracker, updatedIngredientTracker);
                    UserIngredientTrackerHelper.updateIngredientTrackerDetails(existingIngredientTracker,updatedIngredientTracker);
                    return userIngredientTrackerService.save(existingIngredientTracker)
                            .map(savedIngredient -> ResponseEntity.ok(userIngredientTrackerResponseMapper.UserIToDTOUserITRes(savedIngredient)));
                });
    }

    @DeleteMapping("/ingredientTrackers/{ingredientTrackerId}")
    @Operation(summary = "Delete a ingredient tracker", description = "Specify the ingredient tracker ID, to delete the ingredient tracker you want.")
    public Mono<ResponseEntity<String>> deleteITByID(@PathVariable String ingredientTrackerId) {
        return userIngredientTrackerService.deleteById(ingredientTrackerId)
                .then(Mono.just(ResponseEntity.ok("Successfully deleted ingredient tracker with ID " + ingredientTrackerId)));
    }
}
