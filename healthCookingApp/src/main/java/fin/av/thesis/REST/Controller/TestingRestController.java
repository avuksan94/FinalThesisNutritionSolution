package fin.av.thesis.REST.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fin.av.thesis.BL.Service.OpenAIService;
import fin.av.thesis.DAL.Document.OpenAI.*;
import fin.av.thesis.DAL.Enum.SupportedLanguage;
import fin.av.thesis.UTIL.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("healthAPI")
public class TestingRestController {
    private final OpenAIService openAIService;
    private final ObjectMapper objectMapper;

    public TestingRestController(OpenAIService openAIService, ObjectMapper objectMapper) {
        this.openAIService = openAIService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/generate-recipe")
    public Mono<ResponseEntity<?>> generateRecipe(@RequestBody RecipePrompt prompt, @PathVariable SupportedLanguage lang) {
        return openAIService.generateRecipes(prompt,lang)
                .map(response -> {
                    try {
                        String json = response.getChoices().getFirst().getMessage().getContent();
                        System.out.println(json);
                        String processedJson = JsonUtil.convertFractionsToDecimal(json);
                        SimpleRecipeResponse recipeResponse = objectMapper.readValue(processedJson, SimpleRecipeResponse.class);
                        return ResponseEntity.ok(recipeResponse);
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Error processing JSON: " + e.getMessage());
                    }
                });
    }

    @PostMapping("/generate-mealPlan")
    public Mono<ResponseEntity<?>> generateMealPlan(@RequestBody RecipePrompt prompt, @PathVariable SupportedLanguage lang) {
        return openAIService.generateDailyMealPlan(prompt,lang)
                .map(response -> {
                    try {
                        String json = response.getChoices().getFirst().getMessage().getContent();
                        System.out.println(json);
                        String processedJson = JsonUtil.convertFractionsToDecimal(json);
                        MealPlanResponse mealPlanResponse = objectMapper.readValue(processedJson, MealPlanResponse.class);
                        return ResponseEntity.ok(mealPlanResponse);
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Error processing JSON: " + e.getMessage());
                    }
                });
    }

    @PostMapping("/generate-dietCheck")
    public Mono<ResponseEntity<?>> generateDietCheck(@RequestBody DietPrompt prompt, SupportedLanguage lang) {
        return openAIService.checkDietCompatibility(prompt, lang)
                .map(response -> {
                    try {
                        String json = response.getChoices().getFirst().getMessage().getContent();
                        System.out.println(json);
                        String processedJson = JsonUtil.convertFractionsToDecimal(json);
                        HealthWarningResponse healthWarningResponse = objectMapper.readValue(processedJson, HealthWarningResponse.class);
                        return ResponseEntity.ok(healthWarningResponse);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage());
                    }
                });
    }
}