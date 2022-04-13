package io.rently.searchservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Errors {
    public static final ResponseStatusException INVALID_LAT = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid geolocation value. Make sure the latitude `lat` value is between -90 to 90");
    public static final ResponseStatusException INVALID_LON = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid geolocation value. Make sure the longitude `lon` value is between -180 to 180");
    public static final ResponseStatusException NO_PARAMS = new ResponseStatusException(HttpStatus.BAD_REQUEST, "No query parameters found. Possible query parameters include `lat`, `lon`, `country`, `city`, `zip`, `range`, `count`, or `offset");
    public static final ResponseStatusException MISSING_PARAMS = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid combination of query parameters. Could not make sense of request");
    public static final ResponseStatusException NO_ADDRESS_FOUND = new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find address with given parameters");
    public static final ResponseStatusException NO_ADDRESS_PARAMS = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either `city` or `country` needs to be specified in request parameters");;

    public static class HttpMissingQueryParam extends ResponseStatusException {
        public HttpMissingQueryParam(String queryParamName, Class<?> queryParamType) {
            super(HttpStatus.BAD_REQUEST, "Missing query parameter `" + queryParamName +"` of type " + queryParamType.getName() + ". Example: `" + queryParamName + "=<" + queryParamType.getName() + ">");
        }
    }

    public static class HttpMissingPathVar extends ResponseStatusException {
        public HttpMissingPathVar(String pathVarName) {
            super(HttpStatus.BAD_REQUEST, "Missing url encoded path variable `" + pathVarName +"`. Example: `/api/v1/{" + pathVarName + "}");
        }
    }
}
