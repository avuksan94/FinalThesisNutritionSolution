package fin.av.thesis.DAL.Document.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RecipePrompt {
    private String diet;
    private List<String> healthConditions;
    private List<String> allergies;
    private List<String> ingredients;

    public RecipePrompt(String diet, List<String> healthConditions, List<String> allergies, List<String> ingredients) {
        setDiet(diet);
        setHealthConditions(healthConditions);
        setAllergies(allergies);
        this.ingredients = ingredients;
    }

    public void setDiet(String diet) {
        this.diet = convertToUpperCamelCase(diet);
    }

    public void setHealthConditions(List<String> healthConditions) {
        this.healthConditions = transformList(healthConditions);
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = transformList(allergies);
    }

    private List<String> transformList(List<String> inputList) {
        List<String> transformedList = new ArrayList<>();
        for (String value : inputList) {
            transformedList.add(convertToUpperCamelCase(value));
        }
        return transformedList;
    }

    private String convertToUpperCamelCase(String input) {
        return Arrays.stream(input.split("_"))
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
    }
}
