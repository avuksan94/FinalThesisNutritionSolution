package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import fin.av.thesis.DAL.Document.Nutrition.UserProfile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserProfileService {
    Flux<UserProfile> findAll();
    Mono<UserProfile> findById(String id);
    Mono<UserProfile> findByUserId(String userId);
    Mono<UserProfile> findUserProfileByUsername(String username);
    Mono<UserProfile> save(UserProfile obj);
    Mono<Void> deleteById(String id);
}
