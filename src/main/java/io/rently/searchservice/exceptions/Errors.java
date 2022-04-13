package io.rently.searchservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Errors {
    public static final ResponseStatusException INVALID_GEO = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid geolocation values. Make sure the lat value is between -90 to 90 and lon value between -180 to 180");
    public static final ResponseStatusException INTERNAL_SERVER_ERROR = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred. Request could not be completed");

    public static class HttpAddressNotFound extends ResponseStatusException {
        public HttpAddressNotFound(String ...address) {
            super(HttpStatus.NOT_FOUND, "Could not find an address corresponding to '" + String.join(" ", address) + "'");
        }
    }
}
