package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.UserManagement.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User,String> {
    Mono<User> findByUsername(String username);
    @Query("{ 'username' : ?0, 'authorities' : {$exists: true} }")
    Mono<User> findByUsernameWithAuthorities(String username);
}
