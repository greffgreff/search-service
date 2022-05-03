package io.rently.searchservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.rently.searchservice.dtos.enums.QueryType;

import java.util.HashMap;

public class Summary {
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String query;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final int totalResults;
    @JsonProperty
    private final int count;
    @JsonProperty
    private final int offset;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final QueryType queryType;
    @JsonProperty("parameters")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public final HashMap<String, Object> params;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public final String currentPage;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public final String nextPage;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public final String prevPage;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public final int totalPages;

    private Summary(Builder builder) {
        this.query = builder.query;
        this.totalResults = builder.totalResults;
        this.count = builder.count;
        this.offset = builder.offset;
        this.queryType = builder.queryType;
        this.prevPage = builder.prevPage;
        this.totalPages = builder.totalPages;
        this.nextPage = builder.nextPage;
        this.currentPage = builder.currentPage;
        this.params = builder.params;
    }

    public static class Builder {
        private String query;
        private int totalResults;
        private int count;
        private int offset;
        private final QueryType queryType;
        private String prevPage;
        private String currentPage;
        private String nextPage;
        private int totalPages;
        private HashMap<String, Object> params;

        public Builder(QueryType queryType) {
            this.queryType = queryType;
        }

        public Builder setPrevPage(String prevPage) {
            this.prevPage = prevPage;
            return this;
        }

        public Builder setTotalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder setNextPage(String nextPage) {
            this.nextPage = nextPage;
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

        public Builder setParams(HashMap<String, Object> params) {
            this.params = params;
            return this;
        }

        public Summary build() {
            return new Summary(this);
        }
    }
}
