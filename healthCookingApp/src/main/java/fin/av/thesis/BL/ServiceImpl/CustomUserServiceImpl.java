package fin.av.thesis.BL.ServiceImpl;

import fin.av.thesis.DAL.Document.UserManagement.User;
import fin.av.thesis.DAL.Repository.CustomUserRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserServiceImpl implements CustomUserRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    public CustomUserServiceImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<UserDetails> findByUsernameWithAuthorities(String username) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("username").is(username)),
                Aggregation.lookup("authorities", "_id", "userId", "authorities")
        );

        return mongoTemplate.aggregate(aggregation, "users", User.class)
                .single()
                .map(this::convertToUserDetails);
    }

    private UserDetails convertToUserDetails(User user) {
        Set<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuthority().toString()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }
}