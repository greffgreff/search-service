<p>
  <img src="https://github.com/rently-io/search-service/actions/workflows/ci.yml/badge.svg" />
  <img src="https://github.com/rently-io/search-service/actions/workflows/cd.yml/badge.svg" />
</p>

# Search Service

This Spring Boot project is one among other APIs used in the larger Rently project. This service handles various kinds of queries to return a collection of user-created listings saved on a MongoDB database. Only `GET` requests are possible.

Using MongoDB's field indexes, queries can be made based on text and/or geolocation, and/or proximity, through URL query string. A number of endpoints allows different vairation of these searches. An additional multi-purpose endpoint redirects requests depending on query combination. 

Time-based queries (such as availability or duration) could be implemented in future iterations.

Unlike other services in this project, the Search service does not need any authentication to perform searches as listings being returned as stripped down of sensitive data, including leaser id, specific street number and address if any, etc. However, CORS is still enabled so requests can only originate from the website. 

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
    "totalPages": 1
  },
  "results": []
}
```

Response fields:
| **Field**            | **Description**               |
| -------------------- | ----------------------------- |
| `timestamp` timestamp     | Readable timestamp of time of query |
| `status` int | Response status code |
| `summary` [summary object](#summary-object)        | The query summary object as understood by the service |
| `results` [stripped listings array](#stripped-down-listing-object) | A collection of stripped down listings. |

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
  "totalPages": 4
}
```

Summary fields:
| **Field**            | **Description**               |
| -------------------- | ----------------------------- |
|`query` string     | Keyword(s) query, if any |
| `totalResults` int | Total results in the database |
| `count` int        | Maximum results per page, default 20, min 1, max 100 |
| `offset` int       | Current page index, default 0, min 0 |
| `queryType` queryType     | Query type as understood by the service `RANDOM`, `QUERIED`, `QUERIED_NEARBY_GEO`, `QUERIED_NEARBY_ADDRESS`, `QUERIED_AT_ADDRESS` |
| `currentPage` url string       | Current page URL |
| `prevPage` url string       | Previous page URL, if any |
| `nextPage` url string       | Next page URL, if any |
| `totalPages` int       | Maximum number of pages from given query |

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
| `id` uuid string     | Listing id |
| `name` string        | Listing title |
| `price` double       | Daily rental price |
| `image` url string   | Listing cover image URL |
| `startDate` timestamp   | Rental start timestamp |
| `endDate` timestamp   | Rental end timestamp |
| `createdAt` timestamp   | Timestamp of listing creatation |
| `updatedAt` timestamp   | Timestamp of listing latest updates, if any |
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
| `city` string     | Listing city location, if any |
| `zip` string        | Listing zipcode, if any |
| `country` string       | Listing country, if any |

> The address object does not feature street name or street number

<br />

## Request Mappings

### `GET /api/v1//emails/dispatch` for greetings

Dispatches a greeting email to a recepient. Used when a user first opens an account on Rently.

#### URL parameters:

> _none_

#### Request body parameters:

| **Field**            | **Description**               |
| -------------------- | ----------------------------- |
| `type` mail type     | Mail type of value `GREETINGS`|
| `email` email string | The recipiant's email address |
| `name` string        | The recipiant's name          |

#### Dispatched email example:

```json
{
  "name": "something"
}
```
