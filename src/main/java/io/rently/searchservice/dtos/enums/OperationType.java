package io.rently.searchservice.dtos.enums;

public enum OperationType {
    NEARBY("nearby"),
    LOCATION_SPECIFIC("location");

    public final String uriValue;

    OperationType(String uriValue) {
        this.uriValue = uriValue;
    }
}
