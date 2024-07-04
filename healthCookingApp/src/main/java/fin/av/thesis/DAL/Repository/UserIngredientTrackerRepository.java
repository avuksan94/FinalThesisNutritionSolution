package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.Nutrition.UserIngredientTracker;
import fin.av.thesis.DAL.Document.Nutrition.UserProfile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserIngredientTrackerRepository extends ReactiveMongoRepository<UserIngredientTracker,String> {
    Mono<UserIngredientTracker> findByUserId(String userId);
    Flux<UserIngredientTracker> findAllByUserId(String userId);
}
