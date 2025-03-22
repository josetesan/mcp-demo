package es.josetesan.ai.poc.mcp.amadeus.controller;

import es.josetesan.ai.poc.mcp.amadeus.converter.ActivityConverter;
import es.josetesan.ai.poc.mcp.amadeus.model.ActivityResponse;
import es.josetesan.ai.poc.mcp.amadeus.service.AmadeusService;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Activity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/shopping")
@Slf4j
public class ShoppingActivitiesController {

    private final AmadeusService amadeusService;
    private final ActivityConverter activityConverter;

    @Autowired
    public ShoppingActivitiesController(AmadeusService amadeusService,ActivityConverter activityConverter) {
        this.amadeusService = amadeusService;
        this.activityConverter = activityConverter;
    }

    @GetMapping
    public ResponseEntity<ActivityResponse> getPointsOfInterest(
            @RequestParam Double longitude ,
            @RequestParam Double latitude ) {
        try {
            log.info("Calling amadeus with longitude {} and latitude {}", longitude,latitude);
            Activity[] pois = amadeusService.getActivities(longitude, latitude);
            var data = Arrays.stream(pois).map(activityConverter::convert).toList();
            ActivityResponse response = new ActivityResponse(data);
            return ResponseEntity.ok(response);
        } catch (ResponseException e) {
            log.error("Error !", e);
            return ResponseEntity.status(e.getResponse().getStatusCode()).build();
        } catch (Exception e) {
            log.error("Error !", e);
            return ResponseEntity.internalServerError().build();
        }
    }


}
