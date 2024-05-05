package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.Nutrition.HealthWarning;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HealthWarningService {
    Flux<HealthWarning> findAll();
    Mono<HealthWarning> findById(String id);
    Mono<HealthWarning> save(HealthWarning obj);
    Mono<Void> deleteById(String id);
}
