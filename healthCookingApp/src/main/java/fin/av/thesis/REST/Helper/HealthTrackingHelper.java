package fin.av.thesis.REST.Helper;

import fin.av.thesis.DAL.Document.Nutrition.HealthWarning;
import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import fin.av.thesis.DAL.Document.Nutrition.UserHealthTracker;
import fin.av.thesis.DAL.Document.OpenAI.HealthWarningResponse;
import fin.av.thesis.DAL.Document.OpenAI.SimpleRecipeResponse;
import fin.av.thesis.DTO.Request.UserHealthTrackerRequestDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HealthTrackingHelper {
    public static Mono<List<String>> getAllergyListMono(UserHealthTrackerRequestDTO userHt) {
        return Mono.just(userHt.getKnownAllergies())
                .flatMapMany(Flux::fromIterable)
                .map(Enum::name)
                .collectList();
    }

    public static Mono<List<String>> getAllergyListMono(UserHealthTracker userHt) {
        return Mono.just(userHt.getKnownAllergies())
                .flatMapMany(Flux::fromIterable)
                .map(Enum::name)
                .collectList();
    }

    public static Mono<List<String>> getHealthListMono(UserHealthTrackerRequestDTO userHt) {
        return Mono.just(userHt.getHealthConditions())
                .flatMapMany(Flux::fromIterable)
                .map(Enum::name)
                .collectList();
    }

    public static Mono<List<String>> getHealthListMono(UserHealthTracker userHt) {
        return Mono.just(userHt.getHealthConditions())
                .flatMapMany(Flux::fromIterable)
                .map(Enum::name)
                .collectList();
    }

    public static String convertFractionsToDecimal(String json) {
        Pattern pattern = Pattern.compile("(\"quantity\":\\s*)(\\d+)/(\\d+)");
        Matcher matcher = pattern.matcher(json);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            double numerator = Double.parseDouble(matcher.group(2));
            double denominator = Double.parseDouble(matcher.group(3));
            double decimal = numerator / denominator;
            matcher.appendReplacement(sb, matcher.group(1) + decimal);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void updateHealthWarningFromResponse(HealthWarning existingHealthWarning, HealthWarningResponse healthWarningResponse) {
        existingHealthWarning.setWarningMessage(healthWarningResponse.getWarningMessage());
        existingHealthWarning.setDietPros(healthWarningResponse.getDietPros());
        existingHealthWarning.setDietCons(healthWarningResponse.getDietCons());
        existingHealthWarning.setIngredientsToAvoid(healthWarningResponse.getIngredientsToAvoid());
        existingHealthWarning.setAlternativeDiets(healthWarningResponse.getAlternativeDiets());
    }

    public static HealthWarning getHealthWarning(HealthWarningResponse healthWarningResponse) {
        HealthWarning newWarning = new HealthWarning();
        newWarning.setWarningMessage(healthWarningResponse.getWarningMessage());
        newWarning.setDietPros(healthWarningResponse.getDietPros());
        newWarning.setDietCons(healthWarningResponse.getDietCons());
        newWarning.setAlternativeDiets(healthWarningResponse.getAlternativeDiets());
        newWarning.setIngredientsToAvoid(healthWarningResponse.getIngredientsToAvoid());
        return newWarning;
    }

    public static void updateUHTDetails(UserHealthTracker existingUHT, UserHealthTracker updatedUHT) {
        existingUHT.setDiet(updatedUHT.getDiet());
        existingUHT.setKnownAllergies(updatedUHT.getKnownAllergies());
        existingUHT.setHealthConditions(updatedUHT.getHealthConditions());
    }
}
