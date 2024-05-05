package fin.av.thesis.DAL.Document.UserManagement;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @MongoId
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean enabled;
    private String email;
    private String phoneNumber;
    private Set<Authority> authorities;

    public User(String username, String password, Set<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
}
