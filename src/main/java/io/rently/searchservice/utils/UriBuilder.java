package io.rently.searchservice.utils;

import org.springframework.lang.Nullable;

import java.util.*;

public class UriBuilder {
    private final String base;
    private final HashMap<String, Object> queryParameters = new HashMap<>();
    private final List<String> pathVariables = new ArrayList<>();

    private UriBuilder(@Nullable String base) {
        this.base = base;
    }

    public static UriBuilder of(@Nullable String base) {
        return new UriBuilder(base);
    }

    public UriBuilder addParam(String key, @Nullable Object value) {
        if (queryParameters.containsKey(key)) {
            throw new IllegalArgumentException("Query parameter already exists.");
        } else if (Objects.equals(key, "") || key == null) {
            throw new IllegalArgumentException("Path variable cannot be null or an empty string.");
        }
        queryParameters.put(key, value);
        return this;
    }

    public UriBuilder addPathVar(@Nullable String variable) {
        if (pathVariables.contains(variable)) {
            throw new IllegalArgumentException("Path variable already exists.");
        }
        pathVariables.add(variable);
        return this;
    }

    public String create() {
        StringBuilder uri = new StringBuilder();
        uri.append(base);
        for (String variable : pathVariables) {
            if (variable != null) {
                uri.append("/").append(variable);
            }
        }
        if (!queryParameters.isEmpty()) {
            List<String> parsedParams = new ArrayList<>();
            for (Map.Entry<String, Object> param: queryParameters.entrySet()) {
                if (param.getValue() != null) {
                    parsedParams.add(param.getKey() + "=" + param.getValue());
                }
            }
            uri.append("?").append(String.join("&", parsedParams));
        }
        queryParameters.clear();
        pathVariables.clear();
        return uri.toString();
    }
}
