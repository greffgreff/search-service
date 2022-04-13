package io.rently.searchservice.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

@JsonDeserialize(builder = Address.Builder.class)
public class Address {
    @JsonIgnore
    private String street;
    private String city;
    private String zip;
    private String country;
    @JsonIgnore
    private String formattedAddress;
    @JsonIgnore
    private GeoJsonPoint location;

    protected Address() { }

    public Address(Builder builder) {
        this.street = builder.street;
        this.city = builder.city;
        this.zip = builder.zip;
        this.country = builder.country;
        this.formattedAddress = builder.formattedAddress;
        this.location = builder.location;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getStreet() {
        return street;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public GeoJsonPoint getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", location=" + location +
                '}';
    }

    public static class Builder {
        @JsonProperty
        private final String city;
        @JsonProperty
        private final String zip;
        @JsonProperty
        private final String country;
        @JsonProperty
        private String street;
        @JsonProperty
        private final String formattedAddress;
        @JsonProperty
        private final GeoJsonPoint location;

        public Builder(String city, String zip, String country, String formattedAddress, GeoJsonPoint location) {
            this.city = city;
            this.zip = zip;
            this.country = country;
            this.formattedAddress = formattedAddress;
            this.location = location;
        }

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }

        @JsonCreator
        public Address build() {
            return new Address(this);
        }
    }
}
