package fin.av.thesis.BL.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fin.av.thesis.BL.Service.OpenAIService;
import fin.av.thesis.DAL.Document.OpenAI.ChatCompletionResponse;
import fin.av.thesis.DAL.Document.OpenAI.DietPrompt;
import fin.av.thesis.DAL.Document.OpenAI.RecipePrompt;
import fin.av.thesis.DAL.PromptConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class OpenAIServiceImpl implements OpenAIService {
    @Autowired
    private WebClient webClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    @Override
    public Mono<ChatCompletionResponse> generateRecipes(RecipePrompt recipePrompt) {
        return getChatCompletionResponse(buildRecipePrompt(recipePrompt));
    }

    private Mono<ChatCompletionResponse> getChatCompletionResponse(String prompt) {
        return webClient.post()
                .uri(PromptConsts.API_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"model\": \"gpt-3.5-turbo-0125\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}")
                .retrieve()
                .bodyToMono(ChatCompletionResponse.class)
                .onErrorMap(e -> new IllegalStateException("Failed to fetch recipe", e));
    }

    @Override
    public Mono<ChatCompletionResponse> generateDailyMealPlan(RecipePrompt recipePrompt) {
        return getChatCompletionResponse(buildMealPlanPrompt(recipePrompt));
    }

    @Override
    public Mono<ChatCompletionResponse> checkDietCompatibility(DietPrompt dietPrompt) {
        return getChatCompletionResponse(buildDietCompatibilityPrompt(dietPrompt));
    }

    private String buildRecipePrompt(RecipePrompt recipePrompt) {
        String ingredientsFormatted = recipePrompt.getIngredients().stream()
                .map(ingredient -> String.format("{'name': '%s'}", ingredient))
                .collect(Collectors.joining(", "));

        return String.format(
                PromptConsts.HEALTHY_RECIPE_PROMPT_WITH_WARNING_01,
                recipePrompt.getDiet(),
                recipePrompt.getHealthConditions().toString(),
                ingredientsFormatted,
                recipePrompt.getAllergies().toString()
        );
    }

    private String buildMealPlanPrompt(RecipePrompt recipePrompt) {
        String ingredientsFormatted = recipePrompt.getIngredients().stream()
                .map(ingredient -> String.format("{'name': '%s'}", ingredient))
                .collect(Collectors.joining(", "));

        return String.format(
                PromptConsts.MEAL_PLAN_PROMPT_WITH_WARNING_01,
                recipePrompt.getDiet(),
                recipePrompt.getHealthConditions().toString(),
                ingredientsFormatted,
                recipePrompt.getAllergies().toString()
        );
    }

    private String buildDietCompatibilityPrompt(DietPrompt dietPrompt) {
        String healthConditionsFormatted = dietPrompt.getHealthConditions() != null ?
                String.join(", ", dietPrompt.getHealthConditions()) :
                "no specified health conditions";
        String allergiesFormatted = dietPrompt.getAllergies() != null ?
                String.join(", ", dietPrompt.getAllergies()) :
                "no known allergies";
        return String.format(
                PromptConsts.CHECK_DIET_COMPATIBILITY_SPOONACULAR,
                dietPrompt.getDiet(),
                healthConditionsFormatted,
                allergiesFormatted
        );
    }
}
