package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.Nutrition.Nutrients;
import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface UserNutritionTrackerService {
    Flux<UserNutritionTracker> findAll();
    Mono<UserNutritionTracker> findById(String id);
    Mono<UserNutritionTracker> findByUserId(String userId);
    Mono<UserNutritionTracker> findNutritionTrackerByUsername(String username);
    Flux<UserNutritionTracker> getLastSevenDaysTrackers(String username);
    Mono<UserNutritionTracker> updateNutritionalValues(String username, Date forDay, Nutrients nutrients);
    Mono<UserNutritionTracker> findNutritionTrackerByUsernameAndDate(String username, Date forDay);
    Mono<UserNutritionTracker> save(UserNutritionTracker obj);
    Mono<UserNutritionTracker> createOrUpdateTracker(UserNutritionTracker tracker);
    Mono<Boolean> checkTrackerExists(String username, Date forDay);
    Mono<Void> deleteById(String id);
    Mono<Void> deleteByUsername(String username);
}
