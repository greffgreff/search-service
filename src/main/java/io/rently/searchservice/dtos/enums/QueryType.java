package io.rently.searchservice.dtos.enums;

public enum QueryType {
    GENERIC, // random selection
    NEARBY, // range bound selection
    LOCATION_SPECIFIC // match exactly
}
