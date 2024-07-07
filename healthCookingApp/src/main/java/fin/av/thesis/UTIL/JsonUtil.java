package fin.av.thesis.UTIL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtil {
    public static String convertFractionsToDecimalGPT(String json) {
        // Remove leading non-json text(the json issue at the begin of the response)
        int startIndex = json.indexOf("{");
        if (startIndex != -1) {
            json = json.substring(startIndex);
        }

        Pattern fractionPattern = Pattern.compile("(\"quantity\":\\s*)(\\d+)/(\\d+)");
        Matcher fractionMatcher = fractionPattern.matcher(json);
        StringBuffer sb = new StringBuffer();
        while (fractionMatcher.find()) {
            double numerator = Double.parseDouble(fractionMatcher.group(2));
            double denominator = Double.parseDouble(fractionMatcher.group(3));
            double decimal = numerator / denominator;
            fractionMatcher.appendReplacement(sb, fractionMatcher.group(1) + decimal);
        }
        fractionMatcher.appendTail(sb);

        // (the issue with the extra text and charachters)
        String cleanedJson = sb.toString()
                .replaceAll("```", "");
        int endIndex = cleanedJson.lastIndexOf("}");
        if (endIndex != -1) {
            cleanedJson = cleanedJson.substring(0, endIndex + 1);
        }

        return cleanedJson.trim();
    }
}
