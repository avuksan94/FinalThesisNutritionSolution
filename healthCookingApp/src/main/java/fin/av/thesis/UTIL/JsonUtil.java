package fin.av.thesis.UTIL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtil {
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
}
