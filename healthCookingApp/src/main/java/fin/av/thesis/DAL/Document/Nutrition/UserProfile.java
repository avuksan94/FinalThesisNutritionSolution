package fin.av.thesis.DAL.Document.Nutrition;

import fin.av.thesis.DAL.Enum.SupportedLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "user_profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @MongoId
    private String id;

    @Field("user_id")
    private String userId;

    private SupportedLanguage language;
}
