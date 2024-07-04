package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RecipeService {
    Flux<Recipe> findAll();
    Flux<Recipe> findAllByLanguage(String language);
    Mono<Recipe> findById(String id);
    Flux<Recipe> findAllByHealthTrackerId(String healthTrackerId);
    Mono<Recipe> save(Recipe obj);
    Mono<Void> deleteById(String id);
    Mono<Void> deleteByHealthTrackerId(String healthTrackerId);
    Mono<Void> softDeleteById(String id);
}
