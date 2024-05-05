package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.Nutrition.MealPlan;
import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface MealPlanService {
    Flux<MealPlan> findAll();
    Mono<MealPlan> findById(String id);
    Mono<MealPlan> save(MealPlan obj);
    Mono<Void> deleteById(String id);
    Mono<Void> deleteByHealthTrackerId(String healthTrackerId);
    Flux<MealPlan> findAllByHealthTrackerId(String healthTrackerId);
}
