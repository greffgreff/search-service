package io.rently.searchservice.controllers;

import io.rently.searchservice.dtos.Listing;
import io.rently.searchservice.dtos.ResponseContent;
import io.rently.searchservice.dtos.Summary;
import io.rently.searchservice.dtos.enums.QueryType;
import io.rently.searchservice.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class SearchController {

    @Autowired
    public SearchService service;

    @GetMapping("/listings")
    public ResponseContent handleRandomQueries(
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) Integer offset
    ) {
        count = handleCount(count);
        offset = handleOffset(offset);

        List<Listing> listings = service.fetchAny(count, offset);

        Summary summary = new Summary
                .Builder(QueryType.RANDOM)
                .setNumResults(count)
                .setOffset(offset)
                .setTotalResults(listings.size())
                .build();

        return new ResponseContent
                .Builder()
                .setSummary(summary)
                .setData(listings)
                .build();
    }

    @GetMapping("/listings/{query}")
    public ResponseContent handleQueries(
            @PathVariable String query,
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) Integer offset
    ) {
        count = handleCount(count);
        offset = handleOffset(offset);

        service.fetchByQuery(query, count, offset);

        Summary summary = new Summary
                .Builder(QueryType.QUERIED)
                .setQuery(query)
                .setNumResults(count)
                .setOffset(offset)
                .setTotalResults(99999)
                .build();

        return new ResponseContent
                .Builder()
                .setSummary(summary)
                .build();
    }

    @GetMapping("/listings/geocode/{query}")
    public ResponseContent handleNearbyQueriesByGeocode(
            @PathVariable(required = false) String query,
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam Integer range,
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) Integer offset
    ) {
        count = handleCount(count);
        offset = handleOffset(offset);

        List<Listing> listings = service.fetchByQueryAndGeocode(query, lat, lon, range, count, offset);

        Summary summary = new Summary
                .Builder(QueryType.QUERIED_NEARBY)
                .setQuery(query)
                .setNumResults(count)
                .setOffset(offset)
                .setTotalResults(99999)
                .setLon(lon)
                .setLat(lat)
                .build();

        return new ResponseContent
                .Builder()
                .setSummary(summary)
                .setData(listings)
                .build();
    }

    @GetMapping("/listings/location/{query}")
    public ResponseContent handleQueriesByAddress(
            @PathVariable(required = false) String query,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String zip,
            @RequestParam(required = false) Integer range,
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) Integer offset
    ) {
        count = handleCount(count);
        offset = handleOffset(offset);

        List<Listing> listings = service.fetchByQueryAndAddress(query, country, city, zip, range, count, offset);

        Summary summary = new Summary
                .Builder(QueryType.QUERIED_NEARBY)
                .setQuery(query)
                .setNumResults(count)
                .setOffset(offset)
                .setTotalResults(99999)
                .setCountry(country)
                .setCity(city)
                .setZip(zip)
                .build();

        return new ResponseContent
                .Builder()
                .setSummary(summary)
                .setData(listings)
                .build();
    }

    private Integer handleCount(Integer count) {
        if (count == null || count < 1 || count > 50) {
            return 50;
        }
        return count;
    }

    private Integer handleOffset(Integer offset) {
        if (offset == null || offset < 0) {
            return 0;
        }
        return offset;
    }
}
