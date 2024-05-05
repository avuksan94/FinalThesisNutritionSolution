package fin.av.thesis.DAL.Document.UserManagement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fin.av.thesis.DAL.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "authorities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authority {
    @MongoId
    private String id;
    private String userId;
    private Role authority;

    public Authority(String userId, Role authority) {
        this.userId = userId;
        this.authority = authority;
    }
}
