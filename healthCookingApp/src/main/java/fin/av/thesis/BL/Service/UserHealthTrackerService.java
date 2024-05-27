package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.Nutrition.HealthWarning;
import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import fin.av.thesis.DAL.Document.Nutrition.UserHealthTracker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserHealthTrackerService {
    Flux<UserHealthTracker> findAll();
    Mono<UserHealthTracker> findById(String id);
    Mono<UserHealthTracker> findByUserId(String userId);
    Mono<HealthWarning> findHealthWarningByUsername(String username);
    Mono<UserHealthTracker> findHealthTrackerByUsername(String username);
    Mono<Boolean> healthTrackerExistsByUsername(String username);
    Mono<UserHealthTracker> save(UserHealthTracker obj);
    Mono<Void> deleteById(String id);
    Mono<HealthWarning> getHealthWarningForUser(String userId);
    Mono<HealthWarning> createHealthWarningForTracker(String userHealthTrackerId, HealthWarning healthWarning);
    Mono<UserHealthTracker> updateUserHealthTracker(String trackerId, UserHealthTracker update);
}
