package io.rently.searchservice.services;

import io.rently.searchservice.dtos.Listing;
import io.rently.searchservice.interfaces.ListingsRepository;
import io.rently.searchservice.utils.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    public ListingsRepository repository;

    public void fetchByLocation(String query, String country, String city, String zip, Integer count, Integer offset) {
        Broadcaster.info("Fetching listings by location.");
        if (zip != null && country == null) {
            throw new IllegalArgumentException();
        }

        Broadcaster.debug("Pagination: count = " + count + ", offset = " + offset);
        Broadcaster.info("Parameters: country = " + country + ", city = " + city + ", zip = " + zip);

        // logic
    }

    public List<Listing> fetchAny(Integer count, Integer offset) {
        Broadcaster.info("Fetching listings randomly.");
        Broadcaster.debug("Pagination: count = " + count + ", offset = " + offset);
        Pageable pageable = PageRequest.of(offset, count);
        return repository.findAll(pageable).getContent();
    }
}
