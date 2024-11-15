package fin.av.thesis.REST.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fin.av.thesis.BL.Service.*;
import fin.av.thesis.DAL.Document.Nutrition.*;
import fin.av.thesis.DAL.Document.OpenAI.*;
import fin.av.thesis.DAL.Enum.MealType;
import fin.av.thesis.DAL.Enum.SupportedLanguage;
import fin.av.thesis.DTO.Response.MealPlanResponseDTO;
import fin.av.thesis.REST.Helper.HealthTrackingHelper;
import fin.av.thesis.REST.Mapper.ResponseMapper.MealPlanResponseMapper;
import fin.av.thesis.UTIL.JsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("healthAPI")
public class MealPlanGenController {
    private final OpenAIService openAIService;
    private final UserProfileService userProfileService;
    private final MealPlanService mealPlanService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UserHealthTrackerService userHealthTrackerService;
    private final MealPlanResponseMapper mealPlanResponseMapper;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(MealPlanGenController.class);

    public MealPlanGenController(OpenAIService openAIService, UserProfileService userProfileService, MealPlanService mealPlanService, RecipeService recipeService, IngredientService ingredientService, UserHealthTrackerService userHealthTrackerService, MealPlanResponseMapper mealPlanResponseMapper, ObjectMapper objectMapper) {
        this.openAIService = openAIService;
        this.userProfileService = userProfileService;
        this.mealPlanService = mealPlanService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.userHealthTrackerService = userHealthTrackerService;
        this.mealPlanResponseMapper = mealPlanResponseMapper;
        this.objectMapper = objectMapper;
    }


    @GetMapping("/mealPlansGen/all")
    @ResponseBody
    @Operation(summary = "Find all meal plans", description = "Returns all meal plans.")
    public Mono<ResponseEntity<List<MealPlanResponseDTO>>> findAllMealPlans() {
        return mealPlanService.findAll()
                .collectList()
                .map(mealPlans -> ResponseEntity.ok(mealPlans.stream()
                        .map(mealPlanResponseMapper::MealPlanToDTOMealPlanRes)
                        .toList()));
    }

    @GetMapping("/mealPlansGenByUsername/{username}")
    @ResponseBody
    @Operation(summary = "Find all meal plans by username", description = "Returns all meal plans by username.")
    public Mono<ResponseEntity<List<MealPlanResponseDTO>>> findAllMealPlansByUsername(@PathVariable String username) {
        return userHealthTrackerService.findHealthTrackerByUsername(username)
                .flatMap(userHealthTracker ->
                        mealPlanService.findAllByHealthTrackerId(userHealthTracker.getId())
                                .map(mealPlanResponseMapper::MealPlanToDTOMealPlanRes)
                                .collectList()
                                .map(ResponseEntity::ok));
    }

    @GetMapping("/mealPlansGen/{mealPlanId}")
    @ResponseBody
    @Operation(summary = "Find meal plan by ID", description = "Returns a specific meal plan.")
    public Mono<ResponseEntity<MealPlanResponseDTO>> findMealPlanById(@PathVariable String mealPlanId) {
        return mealPlanService.findById(mealPlanId)
                .map(mealPlanResponseMapper::MealPlanToDTOMealPlanRes)
                .map(ResponseEntity::ok);
    }

    //CREATE MEAL PLAN BASED ON USERNAME
    @PostMapping("/mealPlansGenByUsername/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new meal plan", description = "Creates a new meal plan based on the user's health conditions, known allergies, diet preferences, and ingredients, while considering the user's language preference.")
    public Mono<ResponseEntity<?>> createMealPlanUsingUsername(@PathVariable String username, @Valid @RequestBody List<String> ingredients) {
        return userHealthTrackerService.findHealthTrackerByUsername(username)
                .flatMap(healthTracker -> {
                    Mono<List<String>> healthConditions = HealthTrackingHelper.getHealthListMono(healthTracker);
                    Mono<List<String>> knownAllergies = HealthTrackingHelper.getAllergyListMono(healthTracker);
                    Mono<SupportedLanguage> languageMono = getUserLanguage(username);

                    return Mono.zip(healthConditions, knownAllergies, languageMono)
                            .flatMap(tuple -> {
                                RecipePrompt prompt = new RecipePrompt(
                                        healthTracker.getDiet(),
                                        tuple.getT1(),
                                        tuple.getT2(),
                                        ingredients
                                );
                                SupportedLanguage lang = tuple.getT3();
                                return openAIService.generateDailyMealPlan(prompt, lang)
                                        .flatMap(response -> {
                                            try {
                                                String json = response.getChoices().get(0).getMessage().getContent();
                                                String processedJson = JsonUtil.convertFractionsToDecimalGPT(json);
                                                log.info(processedJson);
                                                MealPlanResponse mealResponse = objectMapper.readValue(processedJson, MealPlanResponse.class);

                                                return createMealPlan(mealResponse, healthTracker.getId(), healthTracker.getDiet(), lang)
                                                        .map(savedRecipe -> ResponseEntity.ok().body(savedRecipe));
                                            } catch (JsonProcessingException e) {
                                                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage()));
                                            }
                                        });
                            });
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    private Mono<MealPlan> createMealPlan(MealPlanResponse mealPlanResponse, String healthTrackerId, String diet, SupportedLanguage lang) {
        return Flux.fromIterable(mealPlanResponse.getMeals())
                .flatMap(mealRecipeResponse -> saveMealPlan(mealRecipeResponse, healthTrackerId, diet, lang)
                        .map(recipe -> new MealRecipe(convertMealType(mealRecipeResponse.getMeal()), recipe))
                )
                .collectList()
                .map(mealRecipes -> {
                    MealPlan mealPlan = new MealPlan();
                    mealPlan.setHealthTrackerId(healthTrackerId);
                    mealPlan.setCreatedAt(new Date());
                    mealPlan.setMeals(mealRecipes);
                    return mealPlan;
                })
                .flatMap(mealPlanService::save);
    }

    private Mono<Recipe> saveMealPlan(MealRecipeResponse recipeResponse, String healthTrackerId,String diet, SupportedLanguage lang) {
        return convertAndSaveIngredients(recipeResponse.getIngredients())
                .flatMap(ingredientEntries -> {
                    Recipe recipe = new Recipe();
                    recipe.setHealthTrackerId(healthTrackerId);
                    recipe.setTitle(recipeResponse.getTitle());
                    recipe.setDescription(recipeResponse.getDescription());
                    recipe.setCreatedWith("AI");
                    recipe.setDiet(diet);
                    recipe.setIngredients(ingredientEntries);
                    recipe.setPreparationSteps(recipeResponse.getPreparation());
                    recipe.setNutritionSummary(new NutritionSummary(
                            recipeResponse.getNutrition().getCalories(),
                            recipeResponse.getNutrition().getProtein(),
                            recipeResponse.getNutrition().getCarbs(),
                            recipeResponse.getNutrition().getFats(),
                            recipeResponse.getNutrition().getFiber(),
                            recipeResponse.getNutrition().getSugar()
                    ));
                    recipe.setCookingTime(recipeResponse.getCookingTime());
                    recipe.setServings(recipeResponse.getServings());
                    recipe.setNotes(recipeResponse.getNotes());
                    recipe.setHealthWarning(recipeResponse.getHealthWarning());
                    recipe.setLanguage(lang.toString());

                    return recipeService.save(recipe);
                });
    }


    private MealType convertMealType(String mealTypeString) {
        return MealType.valueOf(mealTypeString.toUpperCase());
    }


    public Mono<List<IngredientEntry>> convertAndSaveIngredients(List<SimpleIngredient> simpleIngredients) {
        return Flux.fromIterable(simpleIngredients)
                .flatMap(this::saveIngredient)
                .collectList();
    }

    private Mono<IngredientEntry> saveIngredient(SimpleIngredient simpleIngredient) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(simpleIngredient.getName());
        ingredient.setNutrients(new Nutrients(0, 0, 0, 0, 0, 0)); // Placeholder for now

        return ingredientService.save(ingredient)
                .map(savedIngredient -> new IngredientEntry(
                        savedIngredient.getId(),
                        simpleIngredient.getName(),
                        simpleIngredient.getQuantity(),
                        simpleIngredient.getUnit(),
                        ""
                ));
    }

    @ResponseBody
    @Operation(summary = "Delete a meal plan", description = "Specify the mealPlan ID, to delete the meal plan you want.")
    @DeleteMapping("/mealPlansGen/{mealPlanId}")
    public Mono<ResponseEntity<String>> deleteMealPlanById(@PathVariable String mealPlanId) {
        return mealPlanService.findById(mealPlanId)
                .flatMap(mealPlan ->
                        Flux.fromIterable(mealPlan.getMeals())
                                .flatMap(mealRecipe ->
                                        Flux.fromIterable(mealRecipe.getRecipe().getIngredients())
                                                .flatMap(ingredientEntry -> ingredientService.deleteById(ingredientEntry.getIngredientId()))
                                                .then(recipeService.deleteById(mealRecipe.getRecipe().getId()))
                                )
                                .then(mealPlanService.deleteById(mealPlanId))
                                .thenReturn(ResponseEntity.ok("Successfully deleted meal plan and all associated recipes and ingredients with ID " + mealPlanId))
                )
                .switchIfEmpty(Mono.just(ResponseEntity.ok("Meal Plan not found with ID: " + mealPlanId)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete meal plan with ID " + mealPlanId + ": " + e.getMessage())));
    }

    @ResponseBody
    @Operation(summary = "Delete a meal plan", description = "Specify the user health tracker ID, to delete the meal plan you want.")
    @DeleteMapping("/mealPlansGenByUHT/{userHealthTrackerId}")
    public Mono<ResponseEntity<String>> deleteAllMealPlansByUHT(@PathVariable String userHealthTrackerId) {
        return mealPlanService.findAllByHealthTrackerId(userHealthTrackerId)
                .flatMap(mealPlan ->
                        Flux.fromIterable(mealPlan.getMeals())
                                .flatMap(mealRecipe ->
                                        Flux.fromIterable(mealRecipe.getRecipe().getIngredients())
                                                .flatMap(ingredientEntry -> ingredientService.deleteById(ingredientEntry.getIngredientId()))
                                                .then(recipeService.deleteById(mealRecipe.getRecipe().getId()))
                                )
                                .then(mealPlanService.deleteById(mealPlan.getId()))
                )
                .collectList()
                .thenReturn(ResponseEntity.ok("Successfully deleted all meal plans and associated recipes and ingredients for Health Tracker ID: " + userHealthTrackerId))
                .switchIfEmpty(Mono.just(ResponseEntity.ok("No meal plans found with Health Tracker ID: " + userHealthTrackerId)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete meal plans for Health Tracker ID " + userHealthTrackerId + ": " + e.getMessage())));
    }

    private Mono<SupportedLanguage> getUserLanguage(String username) {
        return userProfileService.findUserProfileByUsername(username)
                .map(UserProfile::getLanguage)
                .switchIfEmpty(Mono.just(SupportedLanguage.EN));
    }
}
