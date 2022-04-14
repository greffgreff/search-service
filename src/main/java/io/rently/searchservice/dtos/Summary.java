package io.rently.searchservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.rently.searchservice.dtos.enums.QueryType;

public class Summary {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String query;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final int totalResults;
    @JsonProperty
    private final int count;
    @JsonProperty("page")
    private final int offset;
    @JsonProperty
    private final QueryType queryType;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final double lat;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final double lon;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final int range;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final String country;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final String city;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final String zip;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final String address;

    private Summary(Builder builder) {
        this.query = builder.query;
        this.totalResults = builder.totalResults;
        this.count = builder.count;
        this.offset = builder.offset;
        this.lat = builder.lat;
        this.lon = builder.lon;
        this.range = builder.range;
        this.queryType = builder.queryType;
        this.country = builder.country;
        this.city = builder.city;
        this.zip = builder.zip;
        this.address = builder.address;
    }

    public static class Builder {
        private String query;
        private int totalResults;
        private int count;
        private int offset;
        private double lat;
        private double lon;
        private int range;
        private final QueryType queryType;
        private String country;
        private String city;
        private String zip;
        private String address;

        public Builder(QueryType queryType) {
            this.queryType = queryType;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setQuery(String query) {
            this.query = query;
            return this;
        }

        public Builder setTotalResults(int totalResults) {
            this.totalResults = totalResults;
            return this;
        }

        public Builder setCount(int count) {
            this.count = count;
            return this;
        }

        public Builder setOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLon(double lon) {
            this.lon = lon;
            return this;
        }

        public Builder setRange(int range) {
            this.range = range;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setZip(String zip) {
            this.zip = zip;
            return this;
        }

        public Summary build() {
            return new Summary(this);
        }
    }
}
