package es.josetesan.ai.poc.mcp.amadeus.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Activity;
import com.amadeus.resources.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class AmadeusService {

    private final Amadeus amadeus;

    @Autowired
    public AmadeusService(Amadeus amadeus) {
        this.amadeus = amadeus;
    }

    public Location[] getRecommendedLocations(String cityCodes, String travelerCountryCode) throws ResponseException {
        var data = amadeus.referenceData.recommendedLocations.get(
            Params.with("cityCodes", cityCodes)
                  .and("travelerCountryCode", travelerCountryCode)
        );
        return data;
    }

    public Activity[] getActivities(Double longitude, Double latitude) throws ResponseException {
        var data =  amadeus.shopping.activities.get(
                Params.with("latitude", latitude)
                        .and("longitude",longitude)
        );
        return data;
    }
}