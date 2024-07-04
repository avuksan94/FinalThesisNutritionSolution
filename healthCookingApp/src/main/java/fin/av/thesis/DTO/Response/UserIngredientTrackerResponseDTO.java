package fin.av.thesis.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserIngredientTrackerResponseDTO {
    private String id;
    private String userId;
    private String name;
    private double quantity;
    private String unit;
}
