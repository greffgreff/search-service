package io.rently.searchservice.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
    private float lat;
    @JsonIgnore
    private float lon;

    protected Address() { }

    public Address(Builder builder) {
        this.street = builder.street;
        this.city = builder.city;
        this.zip = builder.zip;
        this.country = builder.country;
        this.formattedAddress = builder.formattedAddress;
        this.lat = builder.lat;
        this.lon = builder.lon;
    }

    public String getStreet() {
        return street;
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

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
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
        private final float lat;
        @JsonProperty
        private final float lon;

        public Builder(String city, String zip, String country, String formattedAddress, float lat, float lon) {
            this.city = city;
            this.zip = zip;
            this.country = country;
            this.formattedAddress = formattedAddress;
            this.lat = lat;
            this.lon = lon;
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
