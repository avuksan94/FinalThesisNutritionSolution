package fin.av.thesis.DAL.Document.OpenAI;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthWarningResponse {
    @JsonProperty("warning_message")
    private String warningMessage;
    @JsonProperty("diet_pros")
    private List<String> dietPros;
    @JsonProperty("diet_cons")
    private List<String> dietCons;
    @JsonProperty("alternative_diets")
    private List<String> alternativeDiets;
    @JsonProperty("ingredients_to_avoid")
    private List<String> ingredientsToAvoid;
}
