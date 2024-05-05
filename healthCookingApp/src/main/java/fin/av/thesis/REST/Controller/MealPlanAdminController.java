package fin.av.thesis.REST.Controller;

import fin.av.thesis.BL.Service.MealPlanService;
import fin.av.thesis.DAL.Document.Nutrition.MealPlan;
import fin.av.thesis.DTO.Request.MealPlanRequestDTO;
import fin.av.thesis.DTO.Response.MealPlanResponseDTO;
import fin.av.thesis.REST.Helper.MealPlanHelper;
import fin.av.thesis.REST.Mapper.RequestMapper.MealPlanRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.MealPlanResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("healthAPI")
public class MealPlanAdminController {
    private final MealPlanService mealPlanService;
    private final MealPlanRequestMapper mealPlanRequestMapper;
    private final MealPlanResponseMapper mealPlanResponseMapper;

    public MealPlanAdminController(MealPlanService mealPlanService, MealPlanRequestMapper mealPlanRequestMapper, MealPlanResponseMapper mealPlanResponseMapper) {
        this.mealPlanService = mealPlanService;
        this.mealPlanRequestMapper = mealPlanRequestMapper;
        this.mealPlanResponseMapper = mealPlanResponseMapper;
    }

    @GetMapping("/mealPlans/all")
    @ResponseBody
    @Operation(summary = "Find all meal plans", description = "Returns all meal plans.")
    public Mono<ResponseEntity<List<MealPlanResponseDTO>>> findAllMealPlans() {
        return mealPlanService.findAll()
                .collectList()
                .map(mealPlans -> ResponseEntity.ok(mealPlans.stream()
                        .map(mealPlanResponseMapper::MealPlanToDTOMealPlanRes)
                        .toList()));
    }

    @GetMapping("/mealPlans/{mealPlanId}")
    @ResponseBody
    @Operation(summary = "Find meal plan by ID", description = "Returns a specific meal plan.")
    public Mono<ResponseEntity<MealPlanResponseDTO>> findMealPlanById(@PathVariable String mealPlanId) {
        return mealPlanService.findById(mealPlanId)
                .map(mealPlanResponseMapper::MealPlanToDTOMealPlanRes)
                .map(ResponseEntity::ok);
    }


    @PostMapping("/mealPlans")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new meal plan", description = "Creates a new meal plan and returns the created meal plan.")
    public Mono<ResponseEntity<MealPlanResponseDTO>> createMealPlan(@Valid @RequestBody MealPlanRequestDTO mealPlan) {
        return mealPlanService.save(mealPlanRequestMapper.DTOMealPlanReqToMealPlan(mealPlan))
                .map(mealPlanResponseMapper::MealPlanToDTOMealPlanRes)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/mealPlans/{mealPlanId}")
    @Operation(summary = "Updates an meal plan based on the mealPlanId", description = "Updates meal plan.")
    public Mono<ResponseEntity<MealPlanResponseDTO>> updateMealPlan(@PathVariable String mealPlanId, @Valid @RequestBody MealPlanRequestDTO mealPlanDTO) {
        return mealPlanService.findById(mealPlanId)
                .flatMap(existingMealPlan -> {
                    MealPlan updatedMealPlan = mealPlanRequestMapper.DTOMealPlanReqToMealPlan(mealPlanDTO);
                    MealPlanHelper.updateMealPlan(existingMealPlan,updatedMealPlan);
                    return mealPlanService.save(existingMealPlan)
                            .map(savedMealPlan -> ResponseEntity.ok(mealPlanResponseMapper.MealPlanToDTOMealPlanRes(savedMealPlan)));
                });
    }

    @ResponseBody
    @Operation(summary = "Delete a meal plan", description = "Specify the mealPlan ID, to delete the meal plan you want.")
    @DeleteMapping("/mealPlans/{mealPlanId}")
    public Mono<ResponseEntity<String>> deleteMealPlan(@PathVariable String mealPlanId) {
        return mealPlanService.deleteById(mealPlanId)
                .then(Mono.just(ResponseEntity.ok("Successfully deleted meal plan with ID " + mealPlanId)));
    }

}
