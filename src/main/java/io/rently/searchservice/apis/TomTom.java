package io.rently.searchservice.apis;

import io.rently.searchservice.utils.Broadcaster;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Component
public class TomTom {
    private final String key;

    private TomTom(@Value("${tomtom.key}") String key) {
        this.key = key;
    }

    public Pair<Double, Double> getGeoFromAddress(String address) throws Exception {
        String baseUrl = "https://api.tomtom.com/search/2/geocode/";
        String requestUrl = baseUrl + address.replace(" ", "%20") + ".json?storeResult=false&view=Unified&key=" + key;
        Broadcaster.debug(requestUrl);
        URL url = new URL(requestUrl);
        URLConnection urlConnection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        JSONObject response = new JSONObject(bufferedReader.readLine());
        JSONObject firstResult = (JSONObject) response.getJSONArray("results").get(0);
        JSONObject position = firstResult.getJSONObject("position");
        Pair<Double, Double> geo = Pair.of(position.getDouble("lat"), position.getDouble("lon"));
        Broadcaster.debug("Score = " + firstResult.getDouble("score") + ", lat = " + geo.getFirst() + ", lon = " + geo.getSecond());
        return geo;
    }
}
