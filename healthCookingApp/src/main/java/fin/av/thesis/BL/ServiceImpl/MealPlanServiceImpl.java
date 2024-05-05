package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.MealPlanService;
import fin.av.thesis.DAL.Document.Nutrition.MealPlan;
import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import fin.av.thesis.DAL.Repository.MealPlanRepository;
import fin.av.thesis.UTIL.CustomNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class MealPlanServiceImpl implements MealPlanService {
    private final MealPlanRepository mealPlanRepository;

    public MealPlanServiceImpl(MealPlanRepository mealPlanRepository) {
        this.mealPlanRepository = mealPlanRepository;
    }

    @Override
    public Flux<MealPlan> findAll() {
        return mealPlanRepository.findAll();
    }

    @Override
    public Mono<MealPlan> findById(String id) {
        return mealPlanRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Meal Plan with that ID was not found: " + id)));
    }

    @Override
    public Mono<MealPlan> save(MealPlan obj) {
        return mealPlanRepository.save(obj);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return mealPlanRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return mealPlanRepository.deleteById(id);
                    } else {
                        return Mono.error(
                                new CustomNotFoundException("Meal Plan with that ID was not found: " + id));
                    }
                });
    }

    @Override
    public Mono<Void> deleteByHealthTrackerId(String healthTrackerId) {
        return mealPlanRepository.existsByHealthTrackerId(healthTrackerId)
                .flatMap(exists -> {
                    if (exists) {
                        return mealPlanRepository.deleteByHealthTrackerId(healthTrackerId);
                    } else {
                        return Mono.error(
                                new CustomNotFoundException("Meal Plan for that health tracker ID was not found: "
                                        + healthTrackerId));
                    }
                });
    }

    @Override
    public Flux<MealPlan> findAllByHealthTrackerId(String healthTrackerId) {
        return mealPlanRepository.findAllByHealthTrackerId(healthTrackerId)
                .switchIfEmpty(Mono.error(
                        new CustomNotFoundException("Meal Plans for that user health tracker were not found: " +
                                healthTrackerId)));
    }
}
