package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.BL.Service.UserProfileService;
import fin.av.thesis.DAL.Document.Nutrition.UserProfile;
import fin.av.thesis.DAL.Repository.UserProfileRepository;
import fin.av.thesis.DAL.Repository.UserRepository;
import fin.av.thesis.UTIL.CustomNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Flux<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    @Override
    public Mono<UserProfile> findById(String id) {
        return userProfileRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Profile with that ID was not found: " + id)));
    }

    @Override
    public Mono<UserProfile> findByUserId(String userId) {
        return userProfileRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Profile with that user ID was not found: " + userId)));
    }

    @Override
    public Mono<UserProfile> findUserProfileByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("User with that username was not found: " + username)))
                .flatMap(user -> userProfileRepository.findByUserId(user.getId()));
    }

    @Override
    public Mono<UserProfile> save(UserProfile obj) {
        return userProfileRepository.save(obj);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userProfileRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return userProfileRepository.deleteById(id);
                    } else {
                        return Mono.error(new CustomNotFoundException("User profile with that ID was not found: " + id));
                    }
                });
    }
}
