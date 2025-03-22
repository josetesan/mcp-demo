# Amadeus Recommended Locations API

A Spring Boot application that serves as a wrapper for the Amadeus Recommended Locations API.

## Prerequisites

- Java 21 or later
- Maven 3.6 or later
- Amadeus API Key and Secret

## Configuration

Update the API credentials in `src/main/resources/application.properties`:

```properties
amadeus.api.key=YOUR_API_KEY
amadeus.api.secret=YOUR_API_SECRET
```

## Build

```bash
mvn clean package
```

## Run

```bash
java -jar target/recommended-locations-0.0.1-SNAPSHOT.jar
```

Or using Maven:

```bash
mvn spring-boot:run
```

## API Usage

### Get Recommended Locations

```
GET /api/recommended-locations?cityCodes={cityCodes}&travelerCountryCode={travelerCountryCode}
```

#### Query Parameters

- `cityCodes` (required): City codes to get recommendations for (e.g., "PAR,LON,NYC")
- `travelerCountryCode` (required): The traveler's country code (e.g., "US")

#### Example Request

```
GET /api/recommended-locations?cityCodes=PAR,LON&travelerCountryCode=US
```

#### Example Response

```json
{
  "data": [
    {
      "name": "Paris",
      "type": "location",
      "subType": "city",
      "geoCode": {
        "latitude": 48.85341,
        "longitude": 2.3488
      },
      "iataCode": "PAR",
      "address": {
        "countryName": "France",
        "stateCode": null,
        "stateName": null,
        "cityName": "Paris",
        "postalCode": null
      },
      "score": {
        "value": 0.98
      },
      "tags": {
        "tags": [
          {
            "id": "attractions",
            "name": "Attractions",
            "weight": 0.95
          }
        ]
      },
      "relevance": {
        "value": 0.97
      }
    }
  ],
  "meta": {
    "count": 1,
    "links": {
      "self": ""
    }
  }
}
```

## Error Handling

The API returns appropriate HTTP status codes and error messages for different scenarios:

- `400 Bad Request`: Invalid request parameters
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Unexpected error

## Dependencies

- Spring Boot 3.2.4
- Amadeus Java SDK 7.1.0
- Lombok