package io.rently.searchservice.controllers;

import io.rently.searchservice.dtos.Listing;
import io.rently.searchservice.dtos.ResponseContent;
import io.rently.searchservice.dtos.Summary;
import io.rently.searchservice.dtos.enums.QueryType;
import io.rently.searchservice.exceptions.Errors;
import io.rently.searchservice.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping("/api/v1/listings")
public class SearchController {

    //      /api/v1/listings/random
    //      /api/v1/listings/id/{id} FIXME add missing endpoint and remove get from listing endpoint
    //      /api/v1/listings/search/aggregatedSearch/{query} <- redirecting to other endpoints only
    //      /api/v1/listings/search/{query}
    //      /api/v1/listings/search/address/{query}
    //      /api/v1/listings/search/nearby/address/{query}
    //      /api/v1/listings/search/nearby/geo/{query}

    @Autowired
    public SearchService service;

    @GetMapping({"/aggregatedSearch/{query}", "/aggregatedQuery"})
    public RedirectView handleRedirection(@PathVariable(required = false) String query, @RequestParam Map<String, ?> parameters) {
        String parsedQuery = (query != null ? query : "");
        String parsedParams = parameters.entrySet().stream().map(Object::toString).collect(joining("&"));
        parsedParams = (parsedParams.isEmpty() ? "" : "?" + parsedParams);
        if ((parameters.containsKey("count") && parameters.containsKey("offset") && parameters.size() == 2) || parameters.isEmpty()) {
            return new RedirectView("/api/v1/listings/search/" + parsedQuery + parsedParams);
        } else if (parameters.containsKey("lat") && parameters.containsKey("lon")) {
            return new RedirectView("/api/v1/listings/search/nearby/geo/" + parsedQuery + parsedParams);
        } else if (parameters.containsKey("country") || parameters.containsKey("city") || parameters.containsKey("zip")) {
            return new RedirectView("/api/v1/listings/search/address/" + parsedQuery + parsedParams);
        } else if (parameters.containsKey("address")) {
            return new RedirectView("/api/v1/listings/search/nearby/address/" + parsedQuery + parsedParams);
        }
        throw Errors.INVALID_REQUEST_PARAMS;
    }

    @GetMapping("/random")
    public ResponseContent fetchListingByQuery(
            @RequestParam(required = false, defaultValue = "50") @Min(1) Integer count
    ) {
        List<Listing> listings = service.queryRandomly(count);

        Summary summary = new Summary
                .Builder(QueryType.RANDOM)
                .setCount(count)
                .build();

        return new ResponseContent
                .Builder()
                .setSummary(summary)
                .setData(listings)
                .build();
    }

    @GetMapping({"/search/{query}" , "/search"})
    public ResponseContent fetchListingByQuery(
            @PathVariable(required = false) String query,
            @RequestParam(required = false, defaultValue = "50") @Min(1) Integer count,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset
    ) {
        List<Listing> listings = service.queryListings(query, count, offset);

        Summary summary = new Summary
                .Builder(QueryType.QUERIED)
                .setCount(count)
                .setOffset(offset)
                .setQuery(query)
                .build();

        return new ResponseContent
                .Builder()
                .setSummary(summary)
                .setData(listings)
                .build();
    }

    @GetMapping({"/search/address/{query}", "/search/address"})
    public ResponseContent fetchListingsByQueryAtAddress(
            @PathVariable(required = false) String query,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String zip,
            @RequestParam(required = false, defaultValue = "50") @Min(1) Integer count,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset
    ) {
        List<Listing> listings = service.queryListingsAtAddress(query, country, city, zip, count, offset);

        Summary summary = new Summary
                .Builder(QueryType.QUERIED_AT_ADDRESS)
                .setCount(count)
                .setOffset(offset)
                .setQuery(query)
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

    @GetMapping({"/search/nearby/geo/{query}", "/search/nearby/geo"})
    public ResponseContent fetchListingsByQueryNearbyGeo(
            @PathVariable(required = false) String query,
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam @Positive Integer range,
            @RequestParam(required = false, defaultValue = "50") @Min(1) Integer count,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset
    ) {
        List<Listing> listings = service.queryListingsNearbyGeo(query, lat, lon, range, count, offset);

        Summary summary = new Summary
                .Builder(QueryType.QUERIED_NEARBY_GEO)
                .setCount(count)
                .setOffset(offset)
                .setQuery(query)
                .setLat(lat)
                .setLon(lon)
                .setRange(range)
                .build();

        return new ResponseContent
                .Builder()
                .setSummary(summary)
                .setData(listings)
                .build();
    }

    @GetMapping({"/search/nearby/address/{query}", "/search/nearby/address"})
    public ResponseContent fetchListingsByQueryNearbyAddress(
            @PathVariable(required = false) String query,
            @RequestParam String address,
            @RequestParam @Positive Integer range,
            @RequestParam(required = false, defaultValue = "50") @Min(1) Integer count,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset
    ) {
        List<Listing> listings = service.queryListingsNearbyAddress(query, range, count, offset, address);

        Summary summary = new Summary
                .Builder(QueryType.QUERIED_NEARBY_ADDRESS)
                .setCount(count)
                .setOffset(offset)
                .setQuery(query)
                .setAddress(address)
                .setRange(range)
                .build();

        return new ResponseContent
                .Builder()
                .setSummary(summary)
                .setData(listings)
                .build();
    }
}
