package fin.av.thesis.REST.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fin.av.thesis.BL.Service.IngredientService;
import fin.av.thesis.BL.Service.OpenAIService;
import fin.av.thesis.BL.Service.RecipeService;
import fin.av.thesis.BL.Service.UserHealthTrackerService;
import fin.av.thesis.DAL.Document.Nutrition.*;
import fin.av.thesis.DAL.Document.OpenAI.RecipePrompt;
import fin.av.thesis.DAL.Document.OpenAI.SimpleIngredient;
import fin.av.thesis.DAL.Document.OpenAI.SimpleRecipeResponse;
import fin.av.thesis.DTO.Response.RecipeResponseDTO;
import fin.av.thesis.REST.Helper.HealthTrackingHelper;
import fin.av.thesis.REST.Mapper.RequestMapper.RecipeRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.RecipeResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("healthAPI")
public class RecipeGenController {
    private final OpenAIService openAIService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UserHealthTrackerService userHealthTrackerService;
    private final RecipeResponseMapper recipeResponseMapper;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(RecipeGenController.class);


    public RecipeGenController(OpenAIService openAIService, RecipeService recipeService, IngredientService ingredientService, UserHealthTrackerService userHealthTrackerService, RecipeResponseMapper recipeResponseMapper, ObjectMapper objectMapper) {
        this.openAIService = openAIService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.userHealthTrackerService = userHealthTrackerService;
        this.recipeResponseMapper = recipeResponseMapper;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/recipes/all")
    @ResponseBody
    @Operation(summary = "Find all recipes", description = "Returns all recipes.")
    public Mono<ResponseEntity<List<RecipeResponseDTO>>> findAllRecipes() {
        return recipeService.findAll()
                .collectList()
                .map(recipes -> ResponseEntity.ok(recipes.stream()
                        .map(recipeResponseMapper::RecipeToDTORecipeRes)
                        .toList()));
    }

    @GetMapping("/recipesByHT/{healthTrackerID}")
    @ResponseBody
    @Operation(summary = "Find all recipes for a specific user based on the health tracker id.",
            description = "Returns all recipes for a specific user.")
    public Mono<ResponseEntity<List<?>>> findAllRecipesForUser(@PathVariable String healthTrackerID) {
        return recipeService.findAllByHealthTrackerId(healthTrackerID)
                .collectList()
                .map(recipes -> ResponseEntity.ok(recipes.stream()
                        .map(recipeResponseMapper::RecipeToDTORecipeRes)
                        .toList()));
    }

    //Search Recipes by Username
    @GetMapping("/recipesByUsername/{username}")
    @ResponseBody
    @Operation(summary = "Find all recipes for a specific user based on the username.",
            description = "Returns all recipes for a specific user.")
    public Mono<ResponseEntity<List<RecipeResponseDTO>>> findAllRecipesForUsername(@PathVariable String username) {
        return userHealthTrackerService.findHealthTrackerByUsername(username)
                .flatMap(userHealthTracker ->
                        recipeService.findAllByHealthTrackerId(userHealthTracker.getId())
                                .map(recipeResponseMapper::RecipeToDTORecipeRes)
                                .collectList()
                                .map(ResponseEntity::ok));
    }

    @GetMapping("/recipes/{recipeId}")
    @ResponseBody
    @Operation(summary = "Find recipe by ID", description = "Returns a specific recipe.")
    public Mono<ResponseEntity<RecipeResponseDTO>> findRecipeById(@PathVariable String recipeId) {
        return recipeService.findById(recipeId)
                .map(recipeResponseMapper::RecipeToDTORecipeRes)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/recipesByHT/{healthTrackerID}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new recipe", description = "Creates a new recipe and returns the created recipe.")
    public Mono<ResponseEntity<?>> createRecipeUsingHT(@PathVariable String healthTrackerID, @Valid @RequestBody List<String> ingredients) {
        return userHealthTrackerService.findById(healthTrackerID)
                .flatMap(healthTracker -> {
                    Mono<List<String>> healthConditions = HealthTrackingHelper.getHealthListMono(healthTracker);
                    Mono<List<String>> knownAllergies = HealthTrackingHelper.getAllergyListMono(healthTracker);

                    return Mono.zip(healthConditions, knownAllergies)
                            .flatMap(tuple -> {
                                RecipePrompt prompt = new RecipePrompt(healthTracker.getDiet(), tuple.getT1(), tuple.getT2(), ingredients);
                                return openAIService.generateRecipes(prompt);
                            })
                            .flatMap(response -> {
                                try {
                                    String json = response.getChoices().getFirst().getMessage().getContent();
                                    String processedJson = HealthTrackingHelper.convertFractionsToDecimal(json);
                                    SimpleRecipeResponse simpleRecipeResponse = objectMapper.readValue(processedJson, SimpleRecipeResponse.class);

                                    return saveRecipe(simpleRecipeResponse,healthTracker.getId(), healthTracker.getDiet())
                                            .map(savedRecipe -> ResponseEntity.ok().body(savedRecipe));
                                } catch (JsonProcessingException e) {
                                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage()));
                                }
                            });
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/recipes/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new recipe", description = "Creates a new recipe and returns the created recipe.")
    public Mono<ResponseEntity<?>> createRecipe(@PathVariable String userId, @Valid @RequestBody List<String> ingredients) {
        return userHealthTrackerService.findByUserId(userId)
                .flatMap(healthTracker -> {
                    Mono<List<String>> healthConditions = HealthTrackingHelper.getHealthListMono(healthTracker);
                    Mono<List<String>> knownAllergies = HealthTrackingHelper.getAllergyListMono(healthTracker);

                    return Mono.zip(healthConditions, knownAllergies)
                            .flatMap(tuple -> {
                                RecipePrompt prompt = new RecipePrompt(healthTracker.getDiet(), tuple.getT1(), tuple.getT2(), ingredients);
                                return openAIService.generateRecipes(prompt);
                            })
                            .flatMap(response -> {
                                try {
                                    String json = response.getChoices().getFirst().getMessage().getContent();
                                    String processedJson = HealthTrackingHelper.convertFractionsToDecimal(json);
                                    SimpleRecipeResponse simpleRecipeResponse = objectMapper.readValue(processedJson, SimpleRecipeResponse.class);

                                    return saveRecipe(simpleRecipeResponse,healthTracker.getId(), healthTracker.getDiet())
                                            .map(savedRecipe -> ResponseEntity.ok().body(savedRecipe));
                                } catch (JsonProcessingException e) {
                                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage()));
                                }
                            });
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    //CREATE USING USERNAME
    @PostMapping("/recipesByUsername/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new recipe", description = "Creates a new recipe and returns the created recipe.")
    public Mono<ResponseEntity<?>> createRecipeUsingUsername(@PathVariable String username, @Valid @RequestBody List<String> ingredients) {
        log.info(ingredients.toString());
        if (ingredients.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body("Ingredients list cannot be empty"));
        }
        return userHealthTrackerService.findHealthTrackerByUsername(username)
                .flatMap(healthTracker -> {
                    Mono<List<String>> healthConditions = HealthTrackingHelper.getHealthListMono(healthTracker);
                    Mono<List<String>> knownAllergies = HealthTrackingHelper.getAllergyListMono(healthTracker);

                    return Mono.zip(healthConditions, knownAllergies)
                            .flatMap(tuple -> {
                                RecipePrompt prompt = new RecipePrompt(healthTracker.getDiet(), tuple.getT1(), tuple.getT2(), ingredients);
                                return openAIService.generateRecipes(prompt);
                            })
                            .flatMap(response -> {
                                try {
                                    String json = response.getChoices().getFirst().getMessage().getContent();
                                    String processedJson = HealthTrackingHelper.convertFractionsToDecimal(json);
                                    log.info(processedJson);
                                    SimpleRecipeResponse simpleRecipeResponse = objectMapper.readValue(processedJson, SimpleRecipeResponse.class);

                                    return saveRecipe(simpleRecipeResponse, healthTracker.getId(), healthTracker.getDiet())
                                            .map(savedRecipe -> ResponseEntity.ok().body(savedRecipe));
                                } catch (JsonProcessingException e) {
                                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage()));
                                }
                            });
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    private Mono<Recipe> saveRecipe(SimpleRecipeResponse recipeResponse, String healthTrackerId, String diet) {
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

                    return recipeService.save(recipe);
                });
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
    @Operation(summary = "Delete a recipe", description = "Specify the recipe ID, to delete the recipe you want.")
    @DeleteMapping("/recipesByUser/{recipeId}")
    public Mono<ResponseEntity<String>> deleteRecipe(@PathVariable String recipeId) {
        return recipeService.findById(recipeId)
                .flatMap(recipe -> Flux.fromIterable(recipe.getIngredients())
                        .flatMap(ingredientEntry -> ingredientService.deleteById(ingredientEntry.getIngredientId()))
                        .then(recipeService.deleteById(recipeId))
                        .thenReturn(ResponseEntity.ok("Successfully deleted recipe and all associated ingredients with ID " + recipeId)))
                .switchIfEmpty(Mono.just(ResponseEntity.ok("Recipe not found with ID: " + recipeId)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete recipe with ID " + recipeId + ": " + e.getMessage())));
    }

    @ResponseBody
    @Operation(summary = "Soft delete a recipe", description = "Specify the recipe ID to disassociate the recipe from the user's health tracker without removing it from the database.")
    @DeleteMapping("/recipesByUserSoft/{recipeId}")
    public Mono<ResponseEntity<String>> softDeleteRecipe(@PathVariable String recipeId) {
        return recipeService.softDeleteById(recipeId)
                .thenReturn(ResponseEntity.ok("Recipe disassociated successfully with ID " + recipeId))
                .switchIfEmpty(Mono.just(ResponseEntity.ok("Recipe not found with ID: " + recipeId)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to disassociate recipe with ID " + recipeId + ": " + e.getMessage())));
    }
}
