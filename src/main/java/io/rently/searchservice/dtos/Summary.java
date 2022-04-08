package io.rently.searchservice.dtos;

import io.rently.searchservice.dtos.enums.QueryType;

public class Summary {
    private final String query;
    private final QueryType queryType;
    private final int totalResults;
    private final int numResults;
    private final int offset;

    private Summary(Builder builder) {
        this.query = builder.query;
        this.queryType = builder.queryType;
        this.totalResults = builder.totalResults;
        this.numResults = builder.numResults;
        this.offset = builder.offset;
    }

    public String getQuery() {
        return query;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getNumResults() {
        return numResults;
    }

    public int getOffset() {
        return offset;
    }

    public static class Builder {
        private String query;
        private final QueryType queryType;
        private int totalResults;
        private int numResults;
        private int offset;

        public Builder(QueryType queryType) {
            this.queryType = queryType;
        }

        public Builder() {
            this(QueryType.GENERIC);
        }

        public Builder setQuery(String query) {
            this.query = query;
            return this;
        }

        public Builder setTotalResults(int totalResults) {
            this.totalResults = totalResults;
            return this;
        }

        public Builder setNumResults(int numResults) {
            this.numResults = numResults;
            return this;
        }

        public Builder setOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public Summary build() {
            return new Summary(this);
        }
    }
}
