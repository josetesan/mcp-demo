package es.josetesan.ai.poc.mcp.amadeus.converter;

import com.amadeus.resources.Location;
import es.josetesan.ai.poc.mcp.amadeus.model.Address;
import es.josetesan.ai.poc.mcp.amadeus.model.GeoCode;
import es.josetesan.ai.poc.mcp.amadeus.model.Links;
import es.josetesan.ai.poc.mcp.amadeus.model.Meta;
import es.josetesan.ai.poc.mcp.amadeus.model.RecommendedLocationsResponse;
import es.josetesan.ai.poc.mcp.amadeus.model.Relevance;
import es.josetesan.ai.poc.mcp.amadeus.model.Score;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationConverter {

    public RecommendedLocationsResponse convert(Location[] amadeusLocations) {
        List<es.josetesan.ai.poc.mcp.amadeus.model.Location> locations = Arrays.stream(amadeusLocations)
                .map(this::convertLocation)
                .collect(Collectors.toList());

        Meta meta = new Meta();
        meta.setCount(locations.size());
        
        Links links = new Links();
        links.setSelf(""); // This would be populated with the actual request URL in a real implementation
        meta.setLinks(links);
        
        return new RecommendedLocationsResponse(locations, meta);
    }
    
    private es.josetesan.ai.poc.mcp.amadeus.model.Location convertLocation(Location amadeusLocation) {
        es.josetesan.ai.poc.mcp.amadeus.model.Location location = new es.josetesan.ai.poc.mcp.amadeus.model.Location();
        location.setName(amadeusLocation.getName());
        location.setType(amadeusLocation.getType());
        location.setSubType(amadeusLocation.getSubType());
        location.setIataCode(amadeusLocation.getIataCode());
        
        GeoCode geoCode = new GeoCode();
        if (amadeusLocation.getGeoCode() != null) {
            geoCode.setLatitude(amadeusLocation.getGeoCode().getLatitude());
            geoCode.setLongitude(amadeusLocation.getGeoCode().getLongitude());
        }
        location.setGeoCode(geoCode);
        
        Address address = new Address();
        if (amadeusLocation.getAddress() != null) {
            address.setCountryName(amadeusLocation.getAddress().getCountryName());
            address.setCityName(amadeusLocation.getAddress().getCityName());
            address.setStateCode(amadeusLocation.getAddress().getCityCode());
            address.setStateName(amadeusLocation.getAddress().getRegionCode());
            address.setPostalCode(amadeusLocation.getAddress().getCityCode());
        }
        location.setAddress(address);
        
        Score score = new Score();
        if (amadeusLocation.getRelevance() != null) {
            score.setValue(amadeusLocation.getRelevance());
        }
        location.setScore(score);
        

        Relevance relevance = new Relevance();
        if (amadeusLocation.getRelevance() != null) {
            relevance.setValue(amadeusLocation.getRelevance());
        }
        location.setRelevance(relevance);
        
        return location;
    }
}