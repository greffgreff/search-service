package io.rently.searchservice.controllers;

import io.rently.searchservice.dtos.ResponseContent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    public static final String PREFIX = "/api/v1";

    @GetMapping(PREFIX + "/nearby")
    public ResponseContent handleEmptyGetNearby() {
        return new ResponseContent
                .Builder()
                .setMessage("Welcome. To make a nearby search, please do this...")
                .build();
    }

    @GetMapping(PREFIX + "/location")
    public ResponseContent handleEmptyGetLocation() {
        return new ResponseContent
                .Builder()
                .setMessage("Welcome. To make a location specific search, please do this...")
                .build();
    }

    @GetMapping(PREFIX + "/generic")
    public ResponseContent handleEmptyGetGeneric() {
        return new ResponseContent
                .Builder()
                .setMessage("Welcome. To make a generic search, please do this...")
                .build();
    }
}
