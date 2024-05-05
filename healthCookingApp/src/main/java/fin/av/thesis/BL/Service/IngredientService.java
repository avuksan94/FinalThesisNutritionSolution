package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.Nutrition.Ingredient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IngredientService {
    Flux<Ingredient> findAll();
    Mono<Ingredient> findById(String id);
    Mono<Ingredient> save(Ingredient obj);
    Mono<Void> deleteById(String id);
}
