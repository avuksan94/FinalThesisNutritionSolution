package fin.av.thesis.REST.Controller;

import fin.av.thesis.BL.Service.RecipeService;
import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import fin.av.thesis.DTO.Request.RecipeRequestDTO;
import fin.av.thesis.DTO.Response.RecipeResponseDTO;
import fin.av.thesis.REST.Helper.RecipeHelper;
import fin.av.thesis.REST.Mapper.RequestMapper.RecipeRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.RecipeResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("healthAPI")
public class RecipeAdminController {
    private final RecipeService recipeService;
    private final RecipeRequestMapper recipeRequestMapper;
    private final RecipeResponseMapper recipeResponseMapper;

    public RecipeAdminController(RecipeService recipeService, RecipeRequestMapper recipeRequestMapper, RecipeResponseMapper recipeResponseMapper) {
        this.recipeService = recipeService;
        this.recipeRequestMapper = recipeRequestMapper;
        this.recipeResponseMapper = recipeResponseMapper;
    }

    @GetMapping("/recipesAdmin/all")
    @ResponseBody
    @Operation(summary = "Find all recipes", description = "Returns all recipes.")
    public Mono<ResponseEntity<List<RecipeResponseDTO>>> findAllRecipesAdmin() {
        return recipeService.findAll()
                .collectList()
                .map(recipes -> ResponseEntity.ok(recipes.stream()
                        .map(recipeResponseMapper::RecipeToDTORecipeRes)
                        .toList()));
    }

    @GetMapping("/recipesAdmin/{recipeId}")
    @ResponseBody
    @Operation(summary = "Find recipe by ID", description = "Returns a specific recipe.")
    public Mono<ResponseEntity<RecipeResponseDTO>> findRecipeByIdAdmin(@PathVariable String recipeId) {
        return recipeService.findById(recipeId)
                .map(recipeResponseMapper::RecipeToDTORecipeRes)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/recipesAdmin")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new recipe", description = "Creates a new recipe and returns the created recipe.")
    public Mono<ResponseEntity<RecipeResponseDTO>> createRecipeAdmin(@Valid @RequestBody RecipeRequestDTO recipe) {
        return recipeService.save(recipeRequestMapper.DTORecipeReqToRecipe(recipe))
                .map(recipeResponseMapper::RecipeToDTORecipeRes)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/recipesAdmin/{recipeId}")
    @Operation(summary = "Updates an recipe based on the recipeId", description = "Updates recipe.")
    public Mono<ResponseEntity<RecipeResponseDTO>> updateRecipeAdmin(@PathVariable String recipeId, @Valid @RequestBody RecipeRequestDTO recipeDTO) {
        return recipeService.findById(recipeId)
                .flatMap(existingRecipe -> {
                    Recipe updatedRecipe = recipeRequestMapper.DTORecipeReqToRecipe(recipeDTO);
                    RecipeHelper.updateRecipe(existingRecipe,updatedRecipe);
                    return recipeService.save(existingRecipe)
                            .map(savedRecipe -> ResponseEntity.ok(recipeResponseMapper.RecipeToDTORecipeRes(savedRecipe)));
                });
    }

    @ResponseBody
    @Operation(summary = "Delete a recipe", description = "Specify the recipe ID, to delete the recipe you want.")
    @DeleteMapping("/recipesAdmin/{recipeId}")
    public Mono<ResponseEntity<String>> deleteRecipeAdmin(@PathVariable String recipeId) {
        return recipeService.deleteById(recipeId)
                .then(Mono.just(ResponseEntity.ok("Successfully deleted recipe with ID " + recipeId)));
    }
}
