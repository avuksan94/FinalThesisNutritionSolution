package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RecipeRepository extends ReactiveMongoRepository<Recipe, String> {
    Flux<Recipe> findAllByHealthTrackerId(String healthTrackerId);
    Mono<Void> deleteByHealthTrackerId(String healthTrackerId);
    Mono<Boolean> existsByHealthTrackerId(String healthTrackerId);
}