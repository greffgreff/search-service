package io.rently.searchservice.controllers;

import io.rently.searchservice.dtos.Listing;
import io.rently.searchservice.dtos.ResponseContent;
import io.rently.searchservice.exceptions.Errors;
import io.rently.searchservice.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    //      /api/v1/search/aggregatedQuery/{query} <- redirecting to other endpoints only
    //      /api/v1/search/listings/{query}
    //      /api/v1/search/listings/address/{query}
    //      /api/v1/search/listings/nearby/address/{query}
    //      /api/v1/search/listings/nearby/geo/{query}

    @Autowired
    public SearchService service;

    @GetMapping({"/aggregatedQuery/{query}", "/aggregatedQuery"})
    public RedirectView handleRedirection(@PathVariable(required = false) String query, @RequestParam Map<String, ?> parameters) {
        String parsedQuery = (query != null ? query : "");
        String parsedParams = parameters.entrySet().stream().map(Object::toString).collect(joining("&"));
        parsedParams = (parsedParams.isEmpty() ? "" : "?" + parsedParams);
        if ((parameters.containsKey("count") && parameters.containsKey("offset") && parameters.size() == 2) || parameters.isEmpty()) {
            return new RedirectView("/api/v1/search/listings/" + parsedQuery + parsedParams);
        } else if (parameters.containsKey("lat") && parameters.containsKey("lon")) {
            return new RedirectView("/api/v1/search/listings/nearby/geo/" + parsedQuery + parsedParams);
        } else if (parameters.containsKey("country") || parameters.containsKey("city") || parameters.containsKey("zip")) {
            if (parameters.containsKey("range")) {
                return new RedirectView("/api/v1/search/listings/nearby/address/" + parsedQuery + parsedParams);
            } else {
                return new RedirectView("/api/v1/search/listings/address/" + parsedQuery + parsedParams);
            }
        }
        throw Errors.INVALID_REQUEST_PARAMS;
    }

    @GetMapping({"/listings/{query}" , "/listings"})
    public ResponseContent fetchListingByQuery(
            @PathVariable(required = false) String query,
            @RequestParam(required = false, defaultValue = "50") @Min(1) Integer count,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset
    ) {
        List<Listing> listings = service.queryListings(query, count, offset);
        return new ResponseContent.Builder().setData(listings).build();
    }

    @GetMapping({"/listings/address/{query}", "/listings/address"})
    public ResponseContent fetchListingsByQueryAtAddress(
            @PathVariable(required = false) String query,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String zip,
            @RequestParam(required = false, defaultValue = "50") @Min(1) Integer count,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset
    ) {
        List<Listing> listings = service.queryListingsAtAddress(query, country, city, zip, count, offset);
        return new ResponseContent.Builder().setData(listings).build();
    }

    @GetMapping({"/listings/nearby/geo/{query}", "/listings/nearby/geo"})
    public ResponseContent fetchListingsByQueryNearbyGeo(
            @PathVariable(required = false) String query,
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam Integer range,
            @RequestParam(required = false, defaultValue = "50") @Min(1) Integer count,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset
    ) {
        List<Listing> listings = service.queryListingsNearbyGeo(query, lat, lon, range, count, offset);
        return new ResponseContent.Builder().setData(listings).build();
    }

    @GetMapping({"/listings/nearby/address/{query}", "/listings/nearby/address"})
    public ResponseContent fetchListingsByQueryNearbyAddress(
            @PathVariable(required = false) String query,
            @RequestParam String address,
            @RequestParam Integer range,
            @RequestParam(required = false, defaultValue = "50") @Min(1) Integer count,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset
    ) {
        List<Listing> listings = service.queryListingsNearbyAddress(query, range, count, offset, address);
        return new ResponseContent.Builder().setData(listings).build();
    }
}
