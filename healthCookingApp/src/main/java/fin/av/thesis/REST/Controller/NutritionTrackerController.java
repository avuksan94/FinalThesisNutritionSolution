package fin.av.thesis.REST.Controller;

import fin.av.thesis.BL.Service.UserNutritionTrackerService;
import fin.av.thesis.BL.Service.UserService;
import fin.av.thesis.DAL.Document.Nutrition.Ingredient;
import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DTO.Request.IngredientRequestDTO;
import fin.av.thesis.DTO.Request.UserNutritionTrackerRequestDTO;
import fin.av.thesis.DTO.Response.IngredientResponseDTO;
import fin.av.thesis.DTO.Response.UserNutritionTrackerResponseDTO;
import fin.av.thesis.DTO.Shared.NutrientsDTO;
import fin.av.thesis.REST.Helper.IngredientHelper;
import fin.av.thesis.REST.Mapper.Basic.NutrientsMapper;
import fin.av.thesis.REST.Mapper.RequestMapper.UserNutritionTrackerRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.UserNutritionTrackerResponseMapper;
import fin.av.thesis.UTIL.CustomAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("healthAPI")
public class NutritionTrackerController {
    private final UserNutritionTrackerService userNutritionTrackerService;
    private final UserNutritionTrackerResponseMapper userNutritionTrackerResponseMapper;
    private final UserService userService;
    private final NutrientsMapper nutrientsMapper;

    public NutritionTrackerController(UserNutritionTrackerService userNutritionTrackerService, UserNutritionTrackerResponseMapper userNutritionTrackerResponseMapper, UserService userService, NutrientsMapper nutrientsMapper) {
        this.userNutritionTrackerService = userNutritionTrackerService;
        this.userNutritionTrackerResponseMapper = userNutritionTrackerResponseMapper;
        this.userService = userService;
        this.nutrientsMapper = nutrientsMapper;
    }


    @GetMapping("/nutritionTrackers/all")
    @Operation(summary = "Find all nutrition trackers", description = "Returns all nutrition trackers.")
    public Mono<ResponseEntity<List<UserNutritionTrackerResponseDTO>>> findAllNutritionTrackers() {
        return userNutritionTrackerService.findAll()
                .collectList()
                .map(nutritionTrackers -> ResponseEntity.ok(nutritionTrackers.stream()
                        .map(userNutritionTrackerResponseMapper::UserNToDTOUserNTRes)
                        .toList()));
    }

    @GetMapping("/nutritionTrackers/{nutritionTrackerId}")
    @Operation(summary = "Find nutrition tracker by ID", description = "Returns a specific nutrition tracker.")
    public Mono<ResponseEntity<UserNutritionTrackerResponseDTO>> findNTById(@PathVariable String nutritionTrackerId) {
        return userNutritionTrackerService.findById(nutritionTrackerId)
                .map(userNutritionTrackerResponseMapper::UserNToDTOUserNTRes)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/nutritionTrackersByUsername/{username}")
    @Operation(summary = "Find nutrition tracker by username", description = "Returns a specific nutrition tracker.")
    public Mono<ResponseEntity<UserNutritionTrackerResponseDTO>> findNTByUsername(@PathVariable String username) {
        return userNutritionTrackerService.findNutritionTrackerByUsernameAndDate(username, getCurrentDate().getTime())
                .map(userNutritionTrackerResponseMapper::UserNToDTOUserNTRes)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/nutritionTrackersWeeklyByUsername/{username}")
    @Operation(summary = "Find nutrition tracker by username for the last 7 days", description = "Returns nutrition tracker for the last 7 days.")
    public Mono<ResponseEntity<List<UserNutritionTrackerResponseDTO>>> findNTByUsernameForWeek(@PathVariable String username) {
        return userNutritionTrackerService.getLastSevenDaysTrackers(username)
                .map(userNutritionTrackerResponseMapper::UserNToDTOUserNTRes)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @PostMapping("/nutritionTrackersByUsername/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new nutrition tracker", description = "Creates a new nutrition tracker.")
    public Mono<ResponseEntity<UserNutritionTrackerResponseDTO>> createNT(@PathVariable String username) {
        return userService.findByUsername(username)
                .flatMap(user -> {
                    UserNutritionTracker tracker = setInitialUserNutritionTracker(user);
                    return userNutritionTrackerService.createOrUpdateTracker(tracker);
                })
                .map(userNutritionTrackerResponseMapper::UserNToDTOUserNTRes)
                .map(ResponseEntity::ok)
                .onErrorMap(CustomAlreadyExistsException.class, e -> new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    private static Calendar getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    private UserNutritionTracker setInitialUserNutritionTracker(User user) {
        Calendar cal = getCurrentDate();

        UserNutritionTracker tracker = new UserNutritionTracker();
        tracker.setUserId(user.getId());
        tracker.setTotalCalories(0);
        tracker.setTotalProtein(0);
        tracker.setTotalCarbs(0);
        tracker.setTotalFats(0);
        tracker.setTotalFiber(0);
        tracker.setTotalSugar(0);
        tracker.setForDay(cal.getTime());
        return tracker;
    }

    @PutMapping("/nutritionUpdateTrackersByUsername/{username}")
    @Operation(summary = "Updates an nuttrition tracker based on the username", description = "Updates an nutrition tracker.")
    public Mono<ResponseEntity<UserNutritionTrackerResponseDTO>> updateIngredient(@PathVariable String username, @Valid @RequestBody NutrientsDTO nutrients) {
        Calendar cal = getCurrentDate();
        return userNutritionTrackerService.updateNutritionalValues(
                        username,
                        cal.getTime(),
                        nutrientsMapper.DTONutrientsToNutrients(nutrients))
                .map(userNutritionTrackerResponseMapper::UserNToDTOUserNTRes)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/nutritionTrackers/{nutritionTrackerId}")
    @Operation(summary = "Delete a nutrition tracker", description = "Specify the nutrition tracker ID, to delete the nutrition tracker you want.")
    public Mono<ResponseEntity<String>> deleteNTByID(@PathVariable String nutritionTrackerId) {
        return userNutritionTrackerService.deleteById(nutritionTrackerId)
                .then(Mono.just(ResponseEntity.ok("Successfully deleted nutrition tracker with ID " + nutritionTrackerId)));
    }
}
