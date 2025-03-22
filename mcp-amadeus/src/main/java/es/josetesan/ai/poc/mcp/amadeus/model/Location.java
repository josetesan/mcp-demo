package es.josetesan.ai.poc.mcp.amadeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private String name;
    private String type;
    private String subType;
    private GeoCode geoCode;
    private String iataCode;
    private Address address;
    private Score score;
    private TagList tags;
    private Relevance relevance;
}