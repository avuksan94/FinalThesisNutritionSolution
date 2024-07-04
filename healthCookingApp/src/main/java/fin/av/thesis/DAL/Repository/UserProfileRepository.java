package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.Nutrition.UserProfile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserProfileRepository extends ReactiveMongoRepository<UserProfile,String> {
    Mono<UserProfile> findByUserId(String userId);
}
