package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.IngredientService;
import fin.av.thesis.DAL.Document.Nutrition.Ingredient;
import fin.av.thesis.DAL.Repository.IngredientRepository;
import fin.av.thesis.UTIL.CustomNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Flux<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Mono<Ingredient> findById(String id) {
        return ingredientRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Ingredient with that ID was not found: " + id)));
    }

    @Override
    public Mono<Ingredient> save(Ingredient obj) {
        return ingredientRepository.save(obj);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return ingredientRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return ingredientRepository.deleteById(id);
                    } else {
                        return Mono.error(new CustomNotFoundException("Ingredient with that ID was not found: " + id));
                    }
                });
    }
}
