package es.josetesan.ai.poc.mcp.amadeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String countryName;
    private String stateCode;
    private String stateName;
    private String cityName;
    private String postalCode;
}