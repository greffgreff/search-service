package io.rently.searchservice.controllers;

import io.rently.searchservice.dtos.ResponseContent;
import io.rently.searchservice.dtos.Summary;
import io.rently.searchservice.services.SearchService;
import io.rently.searchservice.utils.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RestController
public class SearchController {
    public static final String PREFIX = "/api/v1";

    @Autowired
    public SearchService service;

    @GetMapping(PREFIX + "/{query}")
    public ResponseContent handleGetRequest(@PathVariable String query) {
        Broadcaster.info("Searching for: " + query);

        Summary summary = new Summary
                .Builder()
                .setNumResults(24)
                .setOffset(0)
                .setTotalResults(124)
                .setQuery(query)
                .build();

        return new ResponseContent.Builder().setSummary(summary).build();
    }
}
