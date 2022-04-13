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

    public List<Listing> queryAny(Integer count, Integer page) {
        Broadcaster.info("Fetching listings randomly. Pagination: count = " + count + ", page = " + page);
        Pageable pageable = PageRequest.of(page, count);
        return repository.findAll(pageable).getContent();
    }

    public List<Listing> query(String query, Integer count, Integer page) {
        Broadcaster.info("Fetching listings by query. Pagination: count = " + count + ", page = " + page);
        Broadcaster.info("Parameters: query = " + query);
        Pageable pageable = PageRequest.of(page, count);
        return repository.queryAny(query, pageable);
    }

    public List<Listing> queryByAddress(String query, String country, String city, String zip, Integer count, Integer page) {
        Broadcaster.info("Fetching listings by query and location. Pagination: count = " + count + ", page = " + page);
        Broadcaster.info("Parameters: query = " + query + ", country = " + country + ", city = " + city + ", zip = " + zip);
        Pageable pageable = PageRequest.of(page, count);
        if (zip != null && country == null && city == null) {
            throw new IllegalArgumentException("Cannot specify `zip` parameter without either `country` or `city` parameters");
        }
        return repository.queryAtAddress(query, country, city, zip, pageable);
    }

    public List<Listing> queryNearbyByAddress(String query, String country, String city, String zip, Integer range, Integer count, Integer page) {
        Broadcaster.info("Fetching listings by query and location. Pagination: count = " + count + ", page = " + page);
        Broadcaster.info("Parameters: query = " + query + ", country = " + country + ", city = " + city + ", zip = " + zip);
        Pageable pageable = PageRequest.of(page, count);
        try {
            Pair<Double, Double> geoCords = TomTom.getGeoFromAddress(country, city, zip);
            return repository.queryAnyNearbyGeoCode(geoCords.getFirst(), geoCords.getSecond(), range, pageable);
        } catch (Exception ex) {
            throw Errors.NO_ADDRESS_FOUND;
        }
    }

    public List<Listing> queryNearbyByGeocode(String query, Double lat, Double lon, Integer range, Integer count, Integer page) {
        Broadcaster.info("Fetching listings by query and geocode. Pagination: count = " + count + ", page = " + page);
        Broadcaster.info("Parameters: lat = " + lat + ", lon = " + lon);
        if (lat > 90 || lat < -90) {
            throw Errors.INVALID_LAT;
        } else if (lon > 180 || lon < -180) {
            throw Errors.INVALID_LON;
        }
        Pageable pageable = PageRequest.of(page, count);
        return repository.queryNearbyGeoCode(query, lon, lat, range, pageable);
    }
}
