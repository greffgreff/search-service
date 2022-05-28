<p>
  <img src="https://github.com/rently-io/search-service/actions/workflows/ci.yml/badge.svg" />
  <img src="https://github.com/rently-io/search-service/actions/workflows/cd.yml/badge.svg" />
</p>

# Search Service

This Spring Boot project is one among other APIs used in the larger Rently project. This service handles various kinds of queries to return a collection of user-created listings saved on a MongoDB database. Only `GET` requests are possible.

Using MongoDB's field indexes, queries can be made based on text and/or geolocation, and/or proximity, through URL query string. A number of endpoints allows different vairation of these searches. An additional multi-purpose endpoint redirects requests depending on query combination. Text queries are perfomed on listing titles and descriptions, while geolocation and nearby searches are performed using a `geometry` field specially formed in the [_GeoJsonPoint_](https://geojson.org/) format present in listing objects on creation with the following shape:

```json
{
  "geometry": {
    "type": "Point",
    "coordinates": [125.6, 10.1]
  }
}
```

Time-based queries (such as availability or duration) could be implemented in future iterations.

Unlike other services in this project, the Search service does not need any authentication to perform searches as listings being returned as stripped down, providing some obfucation. Sensitive data is removed from queries, including leaser id, specific street number and address if any, etc. However, CORS is still enabled so requests can only originate from the website. 

Also contrary to other service where data security this service features extensive debugging messages when performing flawed queries.

This service is deployed on a Heroku instance [here](https://search-service-rently.herokuapp.com/api/v1/listings/random) and its dockerized container [here](https://hub.docker.com/repository/docker/dockeroo80/rently-search-service).

> ⚠️ Please note that the service is currently deployed on a free Heroku instance and needs a few seconds to warm up on first request!

### C2 model
![C2 model](https://i.imgur.com/CqQbDQA.png)

## Objects

### Response Object
```json
{
  "timestamp": "2022-05-28 14:01:09",
  "status": 200,
  "summary": {
    "query": "bbq",
    "totalResults": 2,
    "count": 20,
    "offset": 0,
    "queryType": "QUERIED",
    "currentPage": "http://search-service-rently.herokuapp.com/v1/listings/search/bbq?offset=0",
    "totalPages": 1,
    "parameters": {
      "country": "France",
      "city": "Remelfing"
    }
  },
  "results": []
}
```

Response fields:
| **Field**            | **Description**               |
| -------------------- | ----------------------------- |
| `timestamp` timestamp| Readable timestamp of time of query |
| `status` int         | Response status code          |
| `summary` [summary object](#summary-object) | The query summary object as understood by the service |
| `results` [listings array](#stripped-down-listing-object) | A collection of stripped down listings. |

### Summary Object
```json
{
  "query": "bbq",
  "totalResults": 13,
  "count": 20,
  "offset": 2,
  "queryType": "QUERIED",
  "currentPage": "http://search-service-rently.herokuapp.com/v1/listings/search/bbq?offset=2",
  "prevPage": "http://search-service-rently.herokuapp.com/v1/listings/search/bbq?offset=1",
  "nextPage": "http://search-service-rently.herokuapp.com/v1/listings/search/bbq?offset=3",
  "totalPages": 4,
  "parameters": {
    "country": "France",
    "city": "Remelfing"
  }
}
```

Summary fields:
| **Field**            | **Description**               |
| -------------------- | ----------------------------- |
| `query` string        | Keyword(s) query, if any     |
| `totalResults` int   | Total results in the database |
| `count` int          | Maximum results per page, default 20, min 1, max 100 |
| `offset` int         | Current page index, default 0, min 0 |
| `queryType` queryType| Query type as understood by the service `RANDOM`, `QUERIED`, `QUERIED_NEARBY_GEO`, `QUERIED_NEARBY_ADDRESS`, `QUERIED_AT_ADDRESS` |
| `currentPage` url string | Current page URL          |
| `prevPage` url string | Previous page URL, if any    |
| `nextPage` url string | Next page URL, if any        |
| `totalPages` int      | Maximum number of pages from given query |
| `parameters` dict     | Key value pairs of URl parameters other than `query`, if any |

### Stripped Down Listing Object
```json
{
  "id": "647aa89e-e34b-4186-83e4-48bb6739c528",
  "name": "New BBQ for rent",
  "price": "12",
  "image": "https://images-service-rently.herokuapp.com/api/v1/images/6a4aa7bd-71a0-44be-ada0-c2183305e565",
  "startDate": "1651615200",
  "endDate": "1652306400",
  "createdAt": "1651617084",
  "updatedAt": "1651617084",
  "address": {
    "city": "Rémelfing",
    "zip": "57200",
    "country": "France"
  }
}
```

Listing fields:
| **Field**            | **Description**               |
| -------------------- | ----------------------------- |
| `id` uuid string     | Listing id                    |
| `name` string        | Listing title                 |
| `price` double       | Daily rental price            |
| `image` url string   | Listing cover image URL       |
| `startDate` timestamp| Rental start timestamp        |
| `endDate` timestamp  | Rental end timestamp          |
| `createdAt` timestamp| Timestamp of listing creatation |
| `updatedAt` timestamp| Timestamp of listing latest updates, if any |
| `address` [address object](#address-object) | Timestamp of listing latest updates, if any |

### Address Object
```json
{
  "city": "Rémelfing",
  "zip": "57200",
  "country": "France"
}
```

Address fields:
| **Field**            | **Description**               |
| -------------------- | ----------------------------- |
| `count` int       | Number of results to be returned | false    |
| `offset` int      | Page index                   | false        |
| `city` string        | Listing city location, if any |
| `zip` string         | Listing zipcode, if any       |
| `country` string     | Listing country, if any       |

> The address object does not feature street name or street number

<br />

## Request Mappings

### `GET /api/v1/listings/search/aggregatedSearch/{query}`

A multi-purpose endpoint that redirect requests depending on the request's URL parameters. This endpoint is intented to make the implementation of listing  searches easier withouth dealing with multiple other endpoints.

Redirection is as followed: if there are no query parameters present, then redirect to `/api/v1/listings/search/`, if there is a longitude `lon` and a latitude `lat` specified, redirect to `/api/v1/listings/search/nearby/geo/`, if there is a `country`, and/or `city`, and/or `zip` specified, redirect to `/api/v1/listings/search/address/`, if there is an `address` field specified, redirect to `/api/v1/listings/search/nearby/address/`. Any further query parameter validation is handled at the redirected endpoints (e.g. presence of `range` field when performing neaby searches, valid geo coordinates with `lat` and `lon` fields).

A `400` error is returned when no endpoint is matched.

#### URL parameters:

| **Field**         | **Description**              | **Required** |
| ----------------- | ---------------------------- | :----------: |
| `query` string    | Keyword(s) query             | false        |
| `count` int          | Maximum results per page, default 20, min 1, max 100 | false        |
| `offset` int         | Current page index, default 0, min 0 | false        |
| `lat` double      | Valid latitudinal coordinate | false        |
| `lon` double      | Valid longitudinal coordinate| false        |
| `lon` double      | Valid longitudinal coordinate| false        |
| `country` string  | Country to query             | false        |
| `city` string     | City to query                | false        |
| `zip` string      | Zip to query                 | false        |
| `address` string  | A freeform address           | false        |

#### Return example:

> _NA_

### `GET /api/v1/listings/random`

Returns a random collection of [stripped down listing](#stripped-down-listing) objects.

#### URL parameters:

> _none_

#### Return example:

```json
{
  "timestamp": "2022-05-28 14:41:46",
  "status": 200,
  "summary": {
    "count": 20,
    "totalResults": 2,
    "offset": 0,
    "queryType": "RANDOM",
    "totalPages": 1
  },
  "results": [
    {
      "id": "216809d9-a657-40ce-b6c7-dc02bf97e793",
      "name": "My new listing",
      "price": "123",
      "image": null,
      "startDate": "1652479200",
      "endDate": "1652479200",
      "createdAt": "1652479718",
      "updatedAt": "1652479718",
      "address": {
        "city": "Rémelfing",
        "zip": "57200",
        "country": "France"
      }
    },
    {
      "id": "48411371-22bb-4ea0-b033-5380d570d7ea",
      "name": "My new listing",
      "price": "123",
      "image": null,
      "startDate": "1652479200",
      "endDate": "1652479200",
      "createdAt": "1652479736",
      "updatedAt": "1652479736",
      "address": {
        "city": "Rémelfing",
        "zip": "57200",
        "country": "France"
      }
    }
  ]
}
```

### `GET /api/v1/listings/id/{id}`

Returns a [stripped down listing](#stripped-down-listing) object by id.

#### URL parameters:

| **Field**         | **Description**              | **Required** |
| ----------------- | ---------------------------- | :----------: |
| `id` uuid string  | Valid listing id             |     true     |

#### Return example:

```json
{
  "id": "216809d9-a657-40ce-b6c7-dc02bf97e793",
  "name": "My new listing",
  "price": "123",
  "image": null,
  "startDate": "1652479200",
  "endDate": "1652479200",
  "createdAt": "1652479718",
  "updatedAt": "1652479718",
  "address": {
    "city": "Rémelfing",
    "zip": "57200",
    "country": "France"
  }
}
```

### `GET /api/v1/listings/search/{query}`

Endpoint handling text searches based on keyword(s) alone. Returns a [response](#response-object).

#### URL parameters:

| **Field**         | **Description**              | **Required** |
| ----------------- | ---------------------------- | :----------: |
| `query` string    | Keyword(s)                   |     false     |
| `count` int          | Maximum results per page, default 20, min 1, max 100 | false        |
| `offset` int         | Current page index, default 0, min 0 | false        |

#### Return example:

```json
{
  "timestamp": "2022-05-28 15:29:44",
  "status": 200,
  "summary": {
    "query": "bbq",
    "totalResults": 2,
    "count": 20,
    "offset": 0,
    "queryType": "QUERIED",
    "currentPage": "http://localhost:8082/api/v1/listings/search/bbq?offset=0",
    "totalPages": 1
  },
  "results": [
    {
      "id": "647aa89e-e34b-4186-83e4-48bb6739c528",
      "name": "New BBQ for rent",
      "price": "12",
      "image": "https://images-service-rently.herokuapp.com/api/v1/images/09d22d79-b95a-4d84-a760-a7b2218c5cff",
      "startDate": "1651615200",
      "endDate": "1652306400",
      "createdAt": "1651617084",
      "updatedAt": "1651617084",
      "address": {
        "city": "Rémelfing",
        "zip": "57200",
        "country": "France"
      }
    },
    {
      "id": "09d22d79-b95a-4d84-a760-a7b2218c5cff",
      "name": "Leasing BBQ",
      "price": "123",
      "image": "https://images-service-rently.herokuapp.com/api/v1/images/09d22d79-b95a-4d84-a760-a7b2218c5cff",
      "startDate": "1652479200",
      "endDate": "1652479200",
      "createdAt": "1652540279",
      "updatedAt": "1652540279",
      "address": {
        "city": "Rémelfing",
        "zip": "57200",
        "country": "France"
      }
    }
  ]
}
```

### `GET /search/address/{query}`

Endpoint handling text searches based on keyword(s) and searches where `country`, `city`, or `zip` is are present individually. Returns a [response](#response-object) object of listings **specifically with** said `country`, `city`, or `zip`. At least one of those parameters must be specificed. 

#### URL parameters:

| **Field**         | **Description**              | **Required** |
| ----------------- | ---------------------------- | :----------: |
| `query` string    | Keyword(s) to query          |     false    |
| `count` int          | Maximum results per page, default 20, min 1, max 100 | false        |
| `offset` int         | Current page index, default 0, min 0 | false        |
| `country` int      | Country string              | false        |
| `city` int      | City string                    | false        |
| `zip` int      | Zip string                      | false        |

#### Return example:

```json
{
  "timestamp": "2022-05-28 15:32:54",
  "status": 200,
  "summary": {
    "query": "bbq",
    "totalResults": 2,
    "count": 20,
    "offset": 0,
    "queryType": "QUERIED_AT_ADDRESS",
    "currentPage": "http://localhost:8082/api/v1/listings/search/address/bbq?country=France&offset=0",
    "totalPages": 1,
    "parameters": {
      "country": "France"
    }
  },
  "results": [
    {
      "id": "647aa89e-e34b-4186-83e4-48bb6739c528",
      "name": "New BBQ for rent",
      "price": "12",
      "image": null,
      "startDate": "1651615200",
      "endDate": "1652306400",
      "createdAt": "1651617084",
      "updatedAt": "1651617084",
      "address": {
        "city": "Rémelfing",
        "zip": "57200",
        "country": "France"
      }
    },
    {
      "id": "09d22d79-b95a-4d84-a760-a7b2218c5cff",
      "name": "Leasing BBQ",
      "price": "123",
      "image": "https://images-service-rently.herokuapp.com/api/v1/images/09d22d79-b95a-4d84-a760-a7b2218c5cff",
      "startDate": "1652479200",
      "endDate": "1652479200",
      "createdAt": "1652540279",
      "updatedAt": "1652540279",
      "address": {
        "city": "Rémelfing",
        "zip": "57200",
        "country": "France"
      }
    }
  ]
}
```
