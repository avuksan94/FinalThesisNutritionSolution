package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.HealthWarningService;
import fin.av.thesis.DAL.Document.Nutrition.HealthWarning;
import fin.av.thesis.DAL.Repository.HealthWarningRepository;
import fin.av.thesis.UTIL.CustomNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class HealthWarningServiceImpl implements HealthWarningService {
    private final HealthWarningRepository healthWarningRepository;

    public HealthWarningServiceImpl(HealthWarningRepository healthWarningRepository) {
        this.healthWarningRepository = healthWarningRepository;
    }

    @Override
    public Flux<HealthWarning> findAll() {
        return healthWarningRepository.findAll();
    }

    @Override
    public Mono<HealthWarning> findById(String id) {
        return healthWarningRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Health Warning with that ID was not found: " + id)));

    }

    @Override
    public Mono<HealthWarning> save(HealthWarning obj) {
        return healthWarningRepository.save(obj);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return healthWarningRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return healthWarningRepository.deleteById(id);
                    } else {
                        return Mono.error(new CustomNotFoundException("Health Warning with that ID was not found: " + id));
                    }
                });
    }
}
