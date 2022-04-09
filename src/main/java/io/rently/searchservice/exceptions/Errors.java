package io.rently.searchservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Errors {
    public static final ResponseStatusException INVALID_GEO = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid geolocation values. Make sure the lat value is between -90 to 90 and lon value between -180 to 180");
}
