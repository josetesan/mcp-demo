package es.josetesan.ai.poc.mcp.amadeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedLocationsResponse {
    private List<Location> data;
    private Meta meta;
}