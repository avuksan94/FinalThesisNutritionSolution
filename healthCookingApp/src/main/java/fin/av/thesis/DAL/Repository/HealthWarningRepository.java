package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.Nutrition.HealthWarning;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface HealthWarningRepository extends ReactiveMongoRepository<HealthWarning,String> {
}
