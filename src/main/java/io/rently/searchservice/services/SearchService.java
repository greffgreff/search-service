package io.rently.searchservice.services;

import io.rently.searchservice.exceptions.Errors;
import io.rently.searchservice.interfaces.ListingsRepository;
import io.rently.searchservice.utils.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    public ListingsRepository repository;

    public void fetchByPosition(String query, Float lat, Float lon, Integer range, Integer count, Integer offset) {
        Broadcaster.info("Fetching listings by position.");
        if (lat > 90 || lat < -90 || lon < -180 || lon > 180) {
            throw Errors.INVALID_GEO;
        }
        Broadcaster.debug("Parameters: count = " + count + ", offset = " + offset);
        Broadcaster.debug("Parameters: lat = " + lat + ", lon = " + lon + ", range = " + range);

        // logic
    }

    public void fetchByLocation(String query, String country, String city, String zip, Integer count, Integer offset) {
        Broadcaster.info("Fetching listings by location.");
        if (zip != null && country == null) {
            throw new IllegalArgumentException();
        }

        Broadcaster.debug("Parameters: count = " + count + ", offset = " + offset);
        Broadcaster.info("Parameters: country = " + country + ", city = " + city + ", zip = " + zip);

        // logic
    }

    public void fetchAny(Integer count) {
        Broadcaster.info("Fetching listings randomly.");
        Broadcaster.debug("Parameters: count = " + count);

        // logic
    }
}
