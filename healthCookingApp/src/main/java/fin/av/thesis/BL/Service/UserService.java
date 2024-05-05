package fin.av.thesis.BL.Service;

import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DAL.Enum.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<User> findAll();
    Mono<User> findById(String id);
    Mono<User> findByIdWithAuthorities(String userId);
    Mono<User> findByUsername(String username);
    Mono<User> save(User obj);
    Mono<User> createUserWithRole(User obj, Role role);
    Mono<Void> deleteById(String id);
}
