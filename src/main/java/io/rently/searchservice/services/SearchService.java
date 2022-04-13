package io.rently.searchservice.services;

import io.rently.searchservice.apis.TomTom;
import io.rently.searchservice.dtos.Listing;
import io.rently.searchservice.exceptions.Errors;
import io.rently.searchservice.interfaces.ListingsRepository;
import io.rently.searchservice.utils.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    public ListingsRepository repository;

    public List<Listing> fetchAny(Integer count, Integer page) {
        Broadcaster.info("Fetching listings randomly. Pagination: count = " + count + ", page = " + page);
        Pageable pageable = PageRequest.of(page, count);
        return repository.findAll(pageable).getContent();
    }

    public void fetchByQuery(String query, Integer count, Integer page) {
        Broadcaster.info("Fetching listings by query. Pagination: count = " + count + ", page = " + page);
        if (query == null) {
            throw new IllegalArgumentException("oi not query");
        }
        // FIXME add fetch by query
    }

    public List<Listing> fetchByQueryAndLocation(String query, String country, String city, String zip, Integer range, Integer count, Integer page) {
        Broadcaster.info("Fetching listings by query and location. Pagination: count = " + count + ", page = " + page);
        Broadcaster.info("Parameters: country = " + country + ", city = " + city + ", zip = " + zip);

        if (zip != null && country == null) {
            throw new IllegalArgumentException("oi no zip or country bitch");
        } else if (query == null) {
            throw new IllegalArgumentException("oi not query");
        } else if (range == null) {
            throw new IllegalArgumentException("not range");
        }

        //{"address": {"location": {$near: {$maxDistance: 1000000, $geometry: {type: "Point", coordinates: [7.091800212860107, 49.08848190307617]}}}}}
        Pair<Double, Double> geoCords = null;
        try {
            geoCords =  TomTom.getGeoFromAddress(country, city, zip);
        } catch (Exception ex) {
            throw new Errors.HttpAddressNotFound(country, city, zip);
        }

        return repository.findNearByGeoCode(geoCords.getFirst(), geoCords.getSecond(), range);
    }

    public List<Listing> fetchByQueryAndGeocode(String query, Double lat, Double lon, Integer range, Integer count, Integer page) {
        Broadcaster.info("Fetching listings by query and geocode. Pagination: count = " + count + ", page = " + page);
        if (lat > 90 || lat < -90) {
            throw new IllegalArgumentException("lat not in range");
        } else if (lon > 180 || lon < -180) {
            throw new IllegalArgumentException("lon not in range");
        } else if (range == null) {
            throw new IllegalArgumentException("no range");
        } else if (query == null) {
            throw new IllegalArgumentException("no query");
        }

        Pageable pageable = PageRequest.of(page, count); // FIXME add pagination to queries

        return repository.findNearByGeoCode(lon, lat, range);
    }
}
