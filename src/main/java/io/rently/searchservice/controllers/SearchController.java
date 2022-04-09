package io.rently.searchservice.controllers;

import io.rently.searchservice.dtos.ResponseContent;
import io.rently.searchservice.dtos.Summary;
import io.rently.searchservice.dtos.enums.QueryType;
import io.rently.searchservice.services.SearchService;
import io.rently.searchservice.utils.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class SearchController {
    public static final String PREFIX = "/api/v1";

    @Autowired
    public SearchService service;

    @GetMapping(PREFIX + "/nearby/{query}")
    public ResponseContent handleGetRequest(
            @PathVariable String query,
            @RequestParam float lat,
            @RequestParam float lon,
            @RequestParam int range,
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) Integer offset
    ) {
        count = handleCount(count);
        offset = handleOffset(offset);

        service.fetchByPosition(query, lat, lon, range, count, offset);

        Summary summary = new Summary
                .Builder(QueryType.NEARBY)
                .setQuery(query)
                .setLat(lat)
                .setLon(lon)
                .setRange(range)
                .setNumResults(count)
                .setOffset(offset)
                .setTotalResults(99999)
                .build();

        return new ResponseContent
                .Builder()
                .setSummary(summary)
                .build();
    }

    @GetMapping(PREFIX + "/location/{query}")
    public ResponseContent handleGetRequest(
            @PathVariable String query,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String zip,
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) Integer offset
    ) {
        count = handleCount(count);
        offset = handleOffset(offset);

        service.fetchByLocation(query, country, city, zip, count, offset);

        Summary summary = new Summary
                .Builder(QueryType.LOCATION_SPECIFIC)
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

    @GetMapping(PREFIX + "/generic/{count}")
    public ResponseContent handleGetRequest(@PathVariable(required = false) Integer count) {
        count = handleCount(count);

        service.fetchAny(count);

        Summary summary = new Summary
                .Builder(QueryType.GENERIC)
                .setNumResults(count)
                .build();

        return new ResponseContent
                .Builder()
                .setSummary(summary)
                .build();
    }

    private int handleCount(Integer count) {
        if (count == null || count < 1 || count > 50) {
            return 50;
        }
        return count;
    }

    private int handleOffset(Integer offset) {
        if (offset == null || offset < 0) {
            return 0;
        }
        return offset;
    }
}
