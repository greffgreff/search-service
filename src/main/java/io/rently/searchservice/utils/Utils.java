package io.rently.searchservice.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils {
    private Utils() { }

    public static Double getRadiansFromKm(Integer km) {
        // 200 km = 0.03135711885774796 radians
        // 100km = 0.01567855942 radians
        // 1km = 0.0015678559 radians
        Broadcaster.debug(km * 0.001);
        return km * 0.001;
    }

    public static String getKeywordsFromQuery(String query) {
        List<String> keywords = Arrays.stream(query.split(" ")).toList();
        keywords = keywords.stream().map(keyword -> "(?=.*" + keyword + ")").toList();
        return String.join("", keywords);
    }

    public static <T> List<T> getPaginated(List<T> sourceList, int offset, int count) {
        int fromIndex = offset * count;
        if(sourceList == null || sourceList.size() <= fromIndex){
            return Collections.emptyList();
        }
        return sourceList.subList(fromIndex, Math.min(fromIndex + count, sourceList.size()));
    }
}
