package es.josetesan.ai.poc.mcp.amadeus.controller;

import es.josetesan.ai.poc.mcp.amadeus.converter.LocationConverter;
import es.josetesan.ai.poc.mcp.amadeus.model.RecommendedLocationsResponse;
import es.josetesan.ai.poc.mcp.amadeus.service.AmadeusService;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommended-locations")
public class RecommendedLocationsController {

    private final AmadeusService amadeusService;
    private final LocationConverter locationConverter;

    @Autowired
    public RecommendedLocationsController(AmadeusService amadeusService, LocationConverter locationConverter) {
        this.amadeusService = amadeusService;
        this.locationConverter = locationConverter;
    }

    @GetMapping
    public ResponseEntity<RecommendedLocationsResponse> getRecommendedLocations(
            @RequestParam(required = true) String cityCodes,
            @RequestParam(required = true) String travelerCountryCode) {
        try {
            Location[] locations = amadeusService.getRecommendedLocations(cityCodes, travelerCountryCode);
            RecommendedLocationsResponse response = locationConverter.convert(locations);
            return ResponseEntity.ok(response);
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getResponse().getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}