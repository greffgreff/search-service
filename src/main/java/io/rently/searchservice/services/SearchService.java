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
            throw new Errors.HttpMissingPathVar("query");
        }
        // FIXME add fetch by query
    }

    public List<Listing> fetchByQueryAndAddress(String query, String country, String city, String zip, Integer range, Integer count, Integer page) {
        Broadcaster.info("Fetching listings by query and location. Pagination: count = " + count + ", page = " + page);
        Broadcaster.info("Parameters: country = " + country + ", city = " + city + ", zip = " + zip);
        Pageable pageable = PageRequest.of(page, count);
        if (country == null && city == null && zip == null && range == null) {
            throw Errors.NO_PARAMS;
        }
        if (range != null) {
            // RANGE_SPECIFIC
            Pair<Double, Double> geoCords;
            try {
                geoCords = TomTom.getGeoFromAddress(country, city, zip);
            } catch (Exception ex) {
                throw Errors.NO_ADDRESS_FOUND;
            }
            return repository.findNearByGeoCode(geoCords.getFirst(), geoCords.getSecond(), range, pageable);
        } else {
            // NON_RANGE_SPECIFIC
            if (zip != null && country == null) {
                throw new IllegalArgumentException("Cannot specify `zip` parameter without `country` parameter");
            }
            return repository.findByAddress(country, city, zip, pageable);
        }
    }

    public List<Listing> fetchByQueryAndGeocode(String query, Double lat, Double lon, Integer range, Integer count, Integer page) {
        Broadcaster.info("Fetching listings by query and geocode. Pagination: count = " + count + ", page = " + page);
        if (lat > 90 || lat < -90) {
            throw Errors.INVALID_LAT;
        } else if (lon > 180 || lon < -180) {
            throw Errors.INVALID_LON;
        } else if (range == null) {
            throw new Errors.HttpMissingQueryParam("range", Integer.class);
        }

        Pageable pageable = PageRequest.of(page, count);
        return repository.findNearByGeoCode(lon, lat, range, pageable);
    }
}
