package fin.av.thesis.REST.Controller;

import fin.av.thesis.BL.Service.IngredientService;
import fin.av.thesis.DAL.Document.Nutrition.Ingredient;
import fin.av.thesis.DTO.Request.IngredientRequestDTO;
import fin.av.thesis.DTO.Response.IngredientResponseDTO;
import fin.av.thesis.REST.Mapper.RequestMapper.IngredientRequestMapper;
import fin.av.thesis.REST.Mapper.ResponseMapper.IngredientResponseMapper;
import fin.av.thesis.REST.Helper.IngredientHelper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("healthAPI")
public class IngredientController {
    private final IngredientService ingredientService;
    private final IngredientRequestMapper ingredientRequestMapper;
    private final IngredientResponseMapper ingredientResponseMapper;

    public IngredientController(IngredientService ingredientService, IngredientRequestMapper ingredientRequestMapper, IngredientResponseMapper ingredientResponseMapper) {
        this.ingredientService = ingredientService;
        this.ingredientRequestMapper = ingredientRequestMapper;
        this.ingredientResponseMapper = ingredientResponseMapper;
    }

    @GetMapping("/ingredients/all")
    @Operation(summary = "Find all ingredients", description = "Returns all ingredients.")
    public Mono<ResponseEntity<List<IngredientResponseDTO>>> findAllIngredients() {
        return ingredientService.findAll()
                .collectList()
                .map(ingredients -> ResponseEntity.ok(ingredients.stream()
                        .map(ingredientResponseMapper::IngredientToDTOIngredientRes)
                        .toList()));
    }

    @GetMapping("/ingredients/{ingredientId}")
    @Operation(summary = "Find ingredient by ID", description = "Returns a specific ingredient.")
    public Mono<ResponseEntity<IngredientResponseDTO>> findIngredientById(@PathVariable String ingredientId) {
        return ingredientService.findById(ingredientId)
                .map(ingredientResponseMapper::IngredientToDTOIngredientRes)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/ingredients")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new ingredient", description = "Creates a new ingredient and returns the created ingredient.")
    public Mono<ResponseEntity<IngredientResponseDTO>> createIngredient(@Valid @RequestBody IngredientRequestDTO ingredient) {
        return ingredientService.save(ingredientRequestMapper.DTOIngredientReqToIngredient(ingredient))
                .map(ingredientResponseMapper::IngredientToDTOIngredientRes)
                .map(ResponseEntity::ok);
    }
    @PutMapping("/ingredients/{ingredientId}")
    @Operation(summary = "Updates an ingredient based on the ingredientId", description = "Updates an ingredient.")
    public Mono<ResponseEntity<IngredientResponseDTO>> updateIngredient(@PathVariable String ingredientId, @Valid @RequestBody IngredientRequestDTO ingredientDTO) {
        return ingredientService.findById(ingredientId)
                .flatMap(existingIngredient -> {
                    Ingredient updatedIngredient = ingredientRequestMapper.DTOIngredientReqToIngredient(ingredientDTO);
                    IngredientHelper.updateIngredientDetails(existingIngredient, updatedIngredient);
                    return ingredientService.save(existingIngredient)
                            .map(savedIngredient -> ResponseEntity.ok(ingredientResponseMapper.IngredientToDTOIngredientRes(savedIngredient)));
                });
    }

    @DeleteMapping("/ingredients/{ingredientId}")
    @Operation(summary = "Delete a ingredient", description = "Specify the ingredient ID, to delete the ingredient you want.")
    public Mono<ResponseEntity<String>> deleteIngredient(@PathVariable String ingredientId) {
        return ingredientService.deleteById(ingredientId)
                .then(Mono.just(ResponseEntity.ok("Successfully deleted ingredient with ID " + ingredientId)));
    }
}
