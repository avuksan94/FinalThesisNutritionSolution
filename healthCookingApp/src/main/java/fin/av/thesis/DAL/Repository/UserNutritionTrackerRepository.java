package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Repository
public interface UserNutritionTrackerRepository extends ReactiveMongoRepository<UserNutritionTracker, String> {
    Mono<UserNutritionTracker> findByUserId(String userId);
    Mono<UserNutritionTracker> findByUserIdAndForDay(String userId, Date forDay);
    Mono<Boolean> existsByUserIdAndForDay(String userId, Date forDay);
    // find trackers from the last 7 days for a specific user
    @Query("{'userId': ?0, 'forDay': {$gte: ?1}}")
    Flux<UserNutritionTracker> findByUserIdAndForDayGreaterThanEqual(String userId, Date startDate);
}
