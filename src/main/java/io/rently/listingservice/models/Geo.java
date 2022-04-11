package io.rently.listingservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Arrays;

@JsonDeserialize(builder = Geo.Builder.class)
public class Geo {
    private String type;
    private float[] coordinates;

    protected Geo() { }

    public Geo(Builder builder) {
        this.coordinates = builder.coordinates;
        this.type = builder.type;
    }

    public String getType() {
        return type;
    }

    public float[] getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return "Geo{" +
                "type='" + type + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }

    public static class Builder {
        @JsonProperty
        private final String type;
        @JsonProperty
        private final float[] coordinates;

        public Builder(float[] coordinates, String type) {
            this.coordinates = coordinates;
            this.type = type;
        }

        @JsonCreator
        public Geo build() {
            return new Geo(this);
        }
    }
}