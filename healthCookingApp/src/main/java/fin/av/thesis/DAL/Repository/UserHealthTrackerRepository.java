package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.Nutrition.UserHealthTracker;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserHealthTrackerRepository extends ReactiveMongoRepository<UserHealthTracker,String> {
    Mono<UserHealthTracker> findByUserId(String userId);
}
