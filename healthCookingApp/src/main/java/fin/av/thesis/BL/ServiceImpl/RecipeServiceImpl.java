package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.RecipeService;
import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import fin.av.thesis.DAL.Repository.RecipeRepository;
import fin.av.thesis.UTIL.CustomNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Flux<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Flux<Recipe> findAllByLanguage(String language) {
        return recipeRepository.findAllByLanguage(language);
    }

    @Override
    public Mono<Recipe> findById(String id) {
        return recipeRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Recipe with that ID was not found: " + id)));
    }

    @Override
    public Flux<Recipe> findAllByHealthTrackerId(String healthTrackerId) {
        return recipeRepository.findAllByHealthTrackerId(healthTrackerId)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Recipes for that tracker were not found: " + healthTrackerId)));
    }

    @Override
    public Mono<Recipe> save(Recipe obj) {
        return recipeRepository.save(obj);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return recipeRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return recipeRepository.deleteById(id);
                    } else {
                        return Mono.error(new CustomNotFoundException("Recipe with that ID was not found: " + id));
                    }
                });
    }

    @Override
    public Mono<Void> deleteByHealthTrackerId(String healthTrackerId) {
        return recipeRepository.existsByHealthTrackerId(healthTrackerId)
                .flatMap(exists -> {
                    if (exists) {
                        return recipeRepository.deleteByHealthTrackerId(healthTrackerId);
                    } else {
                        return Mono.error(new CustomNotFoundException("Recipe for that health tracker ID was not found: " + healthTrackerId));
                    }
                });
    }

    @Override
    public Mono<Void> softDeleteById(String id) {
        return recipeRepository.findById(id)
                .flatMap(recipe -> {
                    recipe.setHealthTrackerId(null);
                    return recipeRepository.save(recipe).then();
                })
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Recipe with that ID was not found: " + id)));
    }
}