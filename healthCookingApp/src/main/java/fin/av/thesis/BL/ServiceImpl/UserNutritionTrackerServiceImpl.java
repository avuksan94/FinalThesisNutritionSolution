package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.UserNutritionTrackerService;
import fin.av.thesis.CONFIG.AuthenticationConfig;
import fin.av.thesis.DAL.Document.Nutrition.Nutrients;
import fin.av.thesis.DAL.Document.Nutrition.UserNutritionTracker;
import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DAL.Repository.UserNutritionTrackerRepository;
import fin.av.thesis.DAL.Repository.UserRepository;
import fin.av.thesis.UTIL.CustomAlreadyExistsException;
import fin.av.thesis.UTIL.CustomNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.Date;

@Service
public class UserNutritionTrackerServiceImpl implements UserNutritionTrackerService {
    private static final Logger logger = LoggerFactory.getLogger(UserNutritionTrackerServiceImpl.class);
    private final UserNutritionTrackerRepository userNutritionTrackerRepository;
    private final UserRepository userRepository;

    public UserNutritionTrackerServiceImpl(UserNutritionTrackerRepository userNutritionTrackerRepository, UserRepository userRepository) {
        this.userNutritionTrackerRepository = userNutritionTrackerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Flux<UserNutritionTracker> findAll() {
        return userNutritionTrackerRepository.findAll();
    }

    @Override
    public Mono<UserNutritionTracker> findById(String id) {
        return userNutritionTrackerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Nutrition Tracking with that ID was not found: " + id)));

    }

    @Override
    public Mono<UserNutritionTracker> findByUserId(String userId) {
        return userNutritionTrackerRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Nutrition Tracking for that User was not found: " + userId)));

    }

    @Override
    public Mono<UserNutritionTracker> findNutritionTrackerByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User with that username was not found: " + username)))
                .flatMap(user -> userNutritionTrackerRepository.findByUserId(user.getId()));
    }

    @Override
    public Flux<UserNutritionTracker> getLastSevenDaysTrackers(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User with that username was not found: " + username)))
                .flatMapMany(user -> {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -7);  // 7 days to get the date range
                    Date sevenDaysAgo = cal.getTime();

                    return userNutritionTrackerRepository.findByUserIdAndForDayGreaterThanEqual(user.getId(), sevenDaysAgo);
                });
    }

    @Override
    public Mono<UserNutritionTracker> updateNutritionalValues(String username, Date forDay, Nutrients nutrients) {
        return userRepository.findByUsername(username)
                .flatMap(user -> userNutritionTrackerRepository.findByUserIdAndForDay(user.getId(), forDay)
                        .flatMap(tracker -> {
                            tracker.setTotalCalories(tracker.getTotalCalories() + nutrients.getCalories());
                            tracker.setTotalProtein(tracker.getTotalProtein() + nutrients.getProtein());
                            tracker.setTotalCarbs(tracker.getTotalCarbs() + nutrients.getCarbs());
                            tracker.setTotalFats(tracker.getTotalFats() + nutrients.getCarbs());
                            tracker.setTotalFiber(tracker.getTotalFiber() + nutrients.getFiber());
                            tracker.setTotalSugar(tracker.getTotalSugar() + nutrients.getSugar());

                            return userNutritionTrackerRepository.save(tracker);
                        }))
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    @Override
    public Mono<UserNutritionTracker> findNutritionTrackerByUsernameAndDate(String username, Date forDay) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User with that username was not found: " + username)))
                .flatMap(user -> userNutritionTrackerRepository.findByUserIdAndForDay(user.getId(), forDay))
                .switchIfEmpty(Mono.error(new CustomNotFoundException("No Nutrition Tracker found for this user on this date.")));
    }


    @Override
    public Mono<UserNutritionTracker> save(UserNutritionTracker obj) {
        return userNutritionTrackerRepository.save(obj);
    }

    @Override
    public Mono<UserNutritionTracker> createOrUpdateTracker(UserNutritionTracker tracker) {
        return userRepository.findById(tracker.getUserId())
                .flatMap(user -> userNutritionTrackerRepository.existsByUserIdAndForDay(user.getId(), tracker.getForDay())
                        .flatMap(exists -> {
                            if (exists) {
                                logger.info("TRACKER ALREADY EXISTS FOR TODAY");
                                return Mono.error(new CustomAlreadyExistsException("A nutrition tracker already exists for this user on this day."));
                            } else {
                                return userNutritionTrackerRepository.save(tracker);
                            }
                        }))
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User not found")));
    }

    @Override
    public Mono<Boolean> checkTrackerExists(String username, Date forDay) {
        return userRepository.findByUsername(username)
                .flatMap(user -> userNutritionTrackerRepository.existsByUserIdAndForDay(user.getId(),forDay))
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userNutritionTrackerRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return userNutritionTrackerRepository.deleteById(id);
                    } else {
                        return Mono.error(new CustomNotFoundException("Nutrition Tracking with that ID was not found: " + id));
                    }
                });
    }

    @Override
    public Mono<Void> deleteByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> userNutritionTrackerRepository.findByUserId(user.getId())
                        .flatMap(tracker -> userNutritionTrackerRepository.deleteById(tracker.getId()))
                        .switchIfEmpty(Mono.error(new CustomNotFoundException("No Nutrition Tracker found for user: " + username))))
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User not found: " + username)));
    }
}
