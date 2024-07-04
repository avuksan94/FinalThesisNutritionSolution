package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.OpenAI.ChatCompletionResponse;
import fin.av.thesis.DAL.Document.OpenAI.DietPrompt;
import fin.av.thesis.DAL.Document.OpenAI.RecipePrompt;
import fin.av.thesis.DAL.Enum.SupportedLanguage;
import reactor.core.publisher.Mono;

public interface OpenAIService {
     Mono<ChatCompletionResponse> generateRecipes(RecipePrompt recipePrompt, SupportedLanguage lang);
     Mono<ChatCompletionResponse> generateDailyMealPlan(RecipePrompt recipePrompt, SupportedLanguage lang);
     Mono<ChatCompletionResponse> checkDietCompatibility(DietPrompt dietPrompt, SupportedLanguage lang);
}
