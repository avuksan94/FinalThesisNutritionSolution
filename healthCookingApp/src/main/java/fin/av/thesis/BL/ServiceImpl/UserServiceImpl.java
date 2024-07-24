package fin.av.thesis.BL.ServiceImpl;

import com.mongodb.DuplicateKeyException;
import fin.av.thesis.BL.Service.AuthorityService;
import fin.av.thesis.BL.Service.UserService;
import fin.av.thesis.DAL.Document.UserManagement.Authority;
import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DAL.Enum.Role;
import fin.av.thesis.DAL.Repository.AuthorityRepository;
import fin.av.thesis.DAL.Repository.UserRepository;
import fin.av.thesis.UTIL.CustomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }


    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User with that ID was not found: " + id)));
    }

    @Override
    public Mono<User> findByIdWithAuthorities(String userId) {
        return userRepository.findById(userId)
                .flatMap(user -> authorityRepository.findByUserId(userId)
                        .collectList()
                        .doOnNext(authorities -> user.setAuthorities(new HashSet<>(authorities)))
                        .thenReturn(user));
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(
                        Mono.error(
                                new CustomNotFoundException("User with that Username was not found: " + username)));
    }

    @Override
    public Mono<User> save(User obj) {
        return userRepository.save(obj)
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or email already exists."));
                    }
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<User> createUserWithRole(User obj, Role role) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return userRepository.deleteById(id);
                    } else {
                        return Mono.error(new CustomNotFoundException("User with that ID was not found: " + id));
                    }
                });
    }
}
