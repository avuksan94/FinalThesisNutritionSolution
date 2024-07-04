package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.UserIngredientTrackerService;
import fin.av.thesis.DAL.Document.Nutrition.UserIngredientTracker;
import fin.av.thesis.DAL.Repository.UserIngredientTrackerRepository;
import fin.av.thesis.DAL.Repository.UserRepository;
import fin.av.thesis.UTIL.CustomNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserIngredientTrackerServiceImpl implements UserIngredientTrackerService {
    private final UserIngredientTrackerRepository userIngredientTrackerRepository;
    private final UserRepository userRepository;

    public UserIngredientTrackerServiceImpl(UserIngredientTrackerRepository userIngredientTrackerRepository, UserRepository userRepository) {
        this.userIngredientTrackerRepository = userIngredientTrackerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Flux<UserIngredientTracker> findAll() {
        return userIngredientTrackerRepository.findAll();
    }

    @Override
    public Flux<UserIngredientTracker> findAllByUserName(String username) {
        return userRepository.findByUsername(username)
                .flatMapMany(user -> userIngredientTrackerRepository.findAllByUserId(user.getId()))
                .switchIfEmpty(Flux.error(new CustomNotFoundException("No ingredients found for username: " + username)));
    }

    @Override
    public Mono<UserIngredientTracker> findById(String id) {
        return userIngredientTrackerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Ingredient tracker with that ID was not found: " + id)));

    }

    @Override
    public Mono<UserIngredientTracker> findByUserId(String userId) {
        return userIngredientTrackerRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Ingredient tracker for that User was not found: " + userId)));
    }

    @Override
    public Mono<UserIngredientTracker> save(UserIngredientTracker obj) {
        return userIngredientTrackerRepository.save(obj);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userIngredientTrackerRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return userIngredientTrackerRepository.deleteById(id);
                    } else {
                        return Mono.error(new CustomNotFoundException("Ingredient tracker with that ID was not found: " + id));
                    }
                });
    }

    @Override
    public Mono<Void> deleteByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> userIngredientTrackerRepository.findByUserId(user.getId())
                        .flatMap(tracker -> userIngredientTrackerRepository.deleteById(tracker.getId()))
                        .switchIfEmpty(Mono.error(new CustomNotFoundException("No Ingredient Tracker found for user: " + username))))
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User not found: " + username)));
    }
}
