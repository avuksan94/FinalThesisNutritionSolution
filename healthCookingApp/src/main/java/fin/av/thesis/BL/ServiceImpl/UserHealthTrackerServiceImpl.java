package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.UserHealthTrackerService;
import fin.av.thesis.DAL.Document.Nutrition.HealthWarning;
import fin.av.thesis.DAL.Document.Nutrition.UserHealthTracker;
import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DAL.Repository.HealthWarningRepository;
import fin.av.thesis.DAL.Repository.UserHealthTrackerRepository;
import fin.av.thesis.DAL.Repository.UserRepository;
import fin.av.thesis.UTIL.CustomNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserHealthTrackerServiceImpl implements UserHealthTrackerService {
    private final UserHealthTrackerRepository userHealthTrackerRepository;
    private final HealthWarningRepository healthWarningRepository;
    private final UserRepository userRepository;

    public UserHealthTrackerServiceImpl(UserHealthTrackerRepository userHealthTrackerRepository, HealthWarningRepository healthWarningRepository, UserRepository userRepository) {
        this.userHealthTrackerRepository = userHealthTrackerRepository;
        this.healthWarningRepository = healthWarningRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Flux<UserHealthTracker> findAll() {
        return userHealthTrackerRepository.findAll();
    }

    @Override
    public Mono<UserHealthTracker> findById(String id) {
        return userHealthTrackerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Health Tracking with that ID was not found: " + id)));
    }

    @Override
    public Mono<UserHealthTracker> findByUserId(String userId) {
        return userHealthTrackerRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Health Tracking for that User was not found: " + userId)));

    }

    @Override
    public Mono<HealthWarning> findHealthWarningByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User with that username was not found: " + username)))
                .flatMap(user -> userHealthTrackerRepository.findByUserId(user.getId()))
                .flatMap(userHealthTracker -> healthWarningRepository.findById(userHealthTracker.getHealthWarningId()));
    }

    @Override
    public Mono<UserHealthTracker> findHealthTrackerByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User with that username was not found: " + username)))
                .flatMap(user -> userHealthTrackerRepository.findByUserId(user.getId()));
    }

    @Override
    public Mono<Boolean> healthTrackerExistsByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User with that username was not found: " + username)))
                .flatMap(user -> userHealthTrackerRepository.findByUserId(user.getId())
                        .map(tracker -> true)
                        .defaultIfEmpty(false));
    }

    @Override
    public Mono<HealthWarning> getHealthWarningForUser(String userId) {
        return userHealthTrackerRepository.findById(userId)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User with that ID was not found: " + userId)))
                .flatMap(tracker -> healthWarningRepository.findById(tracker.getHealthWarningId()));
    }

    @Override
    public Mono<HealthWarning> createHealthWarningForTracker(String userHealthTrackerId, HealthWarning healthWarningtoAdd) {
        return userHealthTrackerRepository.findById(userHealthTrackerId)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User Health Tracker not found: " + userHealthTrackerId)))
                .flatMap(tracker -> {
                    HealthWarning healthWarning = new HealthWarning();
                    healthWarning.setWarningMessage(healthWarningtoAdd.getWarningMessage());
                    healthWarning.setDietPros(healthWarningtoAdd.getDietPros());
                    healthWarning.setDietCons(healthWarningtoAdd.getDietCons());
                    healthWarning.setAlternativeDiets(healthWarningtoAdd.getAlternativeDiets());
                    healthWarning.setIngredientsToAvoid(healthWarningtoAdd.getIngredientsToAvoid());
                    return healthWarningRepository.save(healthWarning);
                })
                .doOnSuccess(healthWarning -> linkHealthWarningToTracker(userHealthTrackerId, healthWarning.getId()));
    }

    @Override
    public Mono<UserHealthTracker> updateUserHealthTracker(String trackerId, UserHealthTracker update) {
        return null;
    }

    public Mono<Void> linkHealthWarningToTracker(String trackerId, String healthWarningId) {
        return userHealthTrackerRepository.findById(trackerId)
                .flatMap(tracker -> {
                    tracker.setHealthWarningId(healthWarningId);
                    return userHealthTrackerRepository.save(tracker);
                })
                .then();
    }

    @Override
    public Mono<UserHealthTracker> save(UserHealthTracker obj) {
        return userHealthTrackerRepository.save(obj);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userHealthTrackerRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return userHealthTrackerRepository.deleteById(id);
                    } else {
                        return Mono.error(new CustomNotFoundException("Health Tracking with that ID was not found: " + id));
                    }
                });
    }
}
