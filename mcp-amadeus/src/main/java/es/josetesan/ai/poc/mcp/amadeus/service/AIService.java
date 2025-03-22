package es.josetesan.ai.poc.mcp.amadeus.service;

import es.josetesan.ai.poc.mcp.amadeus.converter.ActivityConverter;
import es.josetesan.ai.poc.mcp.amadeus.converter.LocationConverter;
import es.josetesan.ai.poc.mcp.amadeus.model.Location;
import es.josetesan.ai.poc.mcp.amadeus.model.ShopActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AIService {

    private final AmadeusService amadeusService;
    private final LocationConverter locationConverter;
    private final ActivityConverter activityConverter;

    public AIService(AmadeusService amadeusService, LocationConverter locationConverter, ActivityConverter activityConverter) {
        this.amadeusService = amadeusService;
        this.locationConverter = locationConverter;
        this.activityConverter = activityConverter;
    }

    @Tool(description = "Returns a list of recommendations of cities to travel")
    public List<Location> getRecommendedLocations(@ToolParam(description = "The IATA city code") String cityCode) {
        try {
            log.info("Calling getRecommendedLocations for cityCode {}",cityCode);
            var locations = this.locationConverter.convert(this.amadeusService.getRecommendedLocations(cityCode, "ES")).getData();
            locations
                    .stream().filter(Objects::nonNull)
                    .forEach(location -> log.info("Found {}",location.getName()));
            return locations;
        } catch (Exception e) {
            log.error("Could not return a list of recommendations for {}",cityCode, e);
            throw new RuntimeException(e);
        }
    }

    @Tool(description = "Return a list of shopping activities or recommendations around a place, given its longitude and latitude data")
    public List<ShopActivity> getShoppingActivities(@ToolParam(description = "The longitude of the place") Double longitude, @ToolParam(description = "The latitude of the place") Double latitude) {
        try {
            log.info("Calling getShoppingActivities for longitude:latitude {}:{}",longitude,latitude);
            var result = Arrays.stream(this.amadeusService.getActivities(longitude, latitude))
                    .map(activityConverter::convert)
                    .toList();
            result
                    .stream().filter(Objects::nonNull)
                    .forEach(shopActivity -> log.info("Found {}",shopActivity.name()));
            return result;
        } catch (Exception e) {
            log.error("Could not return a list of shopping activities for longitude:latitude {}:{}",longitude,latitude,e);
            throw new RuntimeException(e);
        }
    }
}
