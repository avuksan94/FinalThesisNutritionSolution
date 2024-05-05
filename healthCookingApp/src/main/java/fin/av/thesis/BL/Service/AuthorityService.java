package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.UserManagement.Authority;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AuthorityService {
    Flux<Authority> findAll();
    Mono<Authority> findById(String id);
    Flux<Authority> findByUserId(String userId);
    Mono<Authority> save(Authority obj);
    Mono<Void> deleteById(String id);
    Mono<Void> deleteByUserId(String userId);
}
