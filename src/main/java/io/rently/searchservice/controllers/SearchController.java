package io.rently.searchservice.controllers;

import io.rently.searchservice.dtos.Listing;
import io.rently.searchservice.dtos.ResponseContent;
import io.rently.searchservice.dtos.Summary;
import io.rently.searchservice.dtos.enums.QueryType;
import io.rently.searchservice.services.SearchService;
import io.rently.searchservice.utils.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {
    public static final String PREFIX = "/api/v1";

    @Autowired
    public SearchService service;

    @GetMapping(PREFIX + "/")
    public ResponseContent handleGetRequest(
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) Integer offset
    ) {
        if (count == null || count < 1 || count > 50) {
            count = 50;
        }
        if (offset == null || offset < 0) {
            offset = 0;
        }

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

    @GetMapping(PREFIX + "/{query}")
    public ResponseContent handleGetRequest(
            @PathVariable String query,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String zip,
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) Integer offset
    ) {
        if (count == null || count < 1 || count > 50) {
            count = 50;
        }
        if (offset == null || offset < 0) {
            offset = 0;
        }

        service.fetchByLocation(query, country, city, zip, count, offset);

        Summary summary = new Summary
                .Builder(QueryType.QUERIED)
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
                .build();
    }
}
