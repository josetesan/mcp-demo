package es.josetesan.ai.poc.mcp.amadeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    private String id;
    private String name;
    private Double weight;
}