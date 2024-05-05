package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.AuthorityService;
import fin.av.thesis.DAL.Document.UserManagement.Authority;
import fin.av.thesis.DAL.Repository.AuthorityRepository;
import fin.av.thesis.UTIL.CustomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }


    @Override
    public Flux<Authority> findAll() {
        return authorityRepository.findAll();
    }

    @Override
    public Mono<Authority> findById(String id) {
        return authorityRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Authority with that ID was not found: " + id)));
    }

    @Override
    public Flux<Authority> findByUserId(String userId) {
        return authorityRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Authority with that User ID was not found: " + userId)));
    }

    @Override
    public Mono<Authority> save(Authority obj) {
        return authorityRepository.save(obj);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return authorityRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return authorityRepository.deleteById(id);
                    } else {
                        return Mono.error(new CustomNotFoundException("Authority with that ID was not found: " + id));
                    }
                });
    }

    @Override
    public Mono<Void> deleteByUserId(String userId) {
        return authorityRepository.deleteByUserId(userId);
    }
}
