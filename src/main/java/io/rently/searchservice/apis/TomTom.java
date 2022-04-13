package io.rently.searchservice.apis;

import org.json.JSONObject;
import org.springframework.data.util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TomTom {
    private static final String TOMTOM_KEY = "r6SBW2lsmjrN88T2GgG7ddAwmtmJiwiC"; // FIXME move to .env
    private static final String BASE_URL = "https://api.tomtom.com/search/2/geocode/";

    private TomTom() { }

    public static Pair<Double, Double> getGeoFromAddress(String ...address) throws Exception {
        URL url = new URL(BASE_URL + String.join("%20", address) + ".json?storeResult=false&view=Unified&key=" + TOMTOM_KEY);
        URLConnection urlConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        JSONObject response = new JSONObject(bufferedReader.readLine());
        Object firstResult = response.getJSONArray("results").get(0);
        JSONObject position = new JSONObject(firstResult).getJSONObject("position");
        return Pair.of(position.getDouble("lon"), position.getDouble("lat"));
    }
}
