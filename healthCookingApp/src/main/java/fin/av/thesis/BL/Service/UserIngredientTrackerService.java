package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.Nutrition.UserIngredientTracker;
import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserIngredientTrackerService {
    Flux<UserIngredientTracker> findAll();
    Flux<UserIngredientTracker> findAllByUserName(String username);
    Mono<UserIngredientTracker> findById(String id);
    Mono<UserIngredientTracker> findByUserId(String userId);
    Mono<UserIngredientTracker> save(UserIngredientTracker obj);
    Mono<Void> deleteById(String id);
    Mono<Void> deleteByUsername(String username);
}
