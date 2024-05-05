package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.OpenAI.ChatCompletionResponse;
import fin.av.thesis.DAL.Document.OpenAI.DietPrompt;
import fin.av.thesis.DAL.Document.OpenAI.RecipePrompt;
import reactor.core.publisher.Mono;

public interface OpenAIService {
     Mono<ChatCompletionResponse> generateRecipes(RecipePrompt recipePrompt);
     Mono<ChatCompletionResponse> generateDailyMealPlan(RecipePrompt recipePrompt);
     Mono<ChatCompletionResponse> checkDietCompatibility(DietPrompt dietPrompt);
}
