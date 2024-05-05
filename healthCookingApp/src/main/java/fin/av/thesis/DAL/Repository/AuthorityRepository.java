package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.UserManagement.Authority;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AuthorityRepository extends ReactiveMongoRepository<Authority,String> {
    Flux<Authority> findByUserId(String userId);
    Mono<Void> deleteByUserId(String userId);
}
