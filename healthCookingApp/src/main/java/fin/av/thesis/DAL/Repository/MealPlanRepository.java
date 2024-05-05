package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.Nutrition.MealPlan;
import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MealPlanRepository extends ReactiveMongoRepository<MealPlan, String> {
    Mono<Void> deleteByHealthTrackerId(String healthTrackerId);
    Mono<Boolean> existsByHealthTrackerId(String healthTrackerId);
    Flux<MealPlan> findAllByHealthTrackerId(String healthTrackerId);
}
