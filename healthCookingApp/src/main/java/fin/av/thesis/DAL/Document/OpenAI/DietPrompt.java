package fin.av.thesis.DAL.Document.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DietPrompt {
    private String diet;
    private List<String> healthConditions;
    private List<String> allergies;
}
