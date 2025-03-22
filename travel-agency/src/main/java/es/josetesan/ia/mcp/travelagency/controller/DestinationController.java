package es.josetesan.ia.mcp.travelagency.controller;

import es.josetesan.ia.mcp.travelagency.domain.Destination;
import es.josetesan.ia.mcp.travelagency.service.DestinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
@Tag(name = "Destination", description = "Destination management APIs")
public class DestinationController {

  private final DestinationService destinationService;

  @GetMapping
  @Operation(summary = "Get all destinations")
  public ResponseEntity<List<Destination>> getAllDestinations() {
    return ResponseEntity.ok(destinationService.getAllDestinations());
  }

  @GetMapping("/available")
  @Operation(summary = "Get all available destinations")
  public ResponseEntity<List<Destination>> getAvailableDestinations() {
    return ResponseEntity.ok(destinationService.getAvailableDestinations());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get destination by ID")
  public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
    return ResponseEntity.ok(destinationService.getDestinationById(id));
  }

  @GetMapping("/country/{country}")
  @Operation(summary = "Get destinations by country")
  public ResponseEntity<List<Destination>> getDestinationsByCountry(@PathVariable String country) {
    return ResponseEntity.ok(destinationService.getDestinationsByCountry(country));
  }

  @GetMapping("/city/{city}")
  @Operation(summary = "Get destinations by city")
  public ResponseEntity<List<Destination>> getDestinationsByCity(@PathVariable String city) {
    return ResponseEntity.ok(destinationService.getDestinationsByCity(city));
  }

  @PostMapping
  @Operation(summary = "Create a new destination")
  public ResponseEntity<Destination> createDestination(
      @Valid @RequestBody Destination destination) {
    Destination createdDestination = destinationService.createDestination(destination);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdDestination);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing destination")
  public ResponseEntity<Destination> updateDestination(
      @PathVariable Long id, @Valid @RequestBody Destination destination) {
    return ResponseEntity.ok(destinationService.updateDestination(id, destination));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a destination")
  public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
    destinationService.deleteDestination(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/toggle-availability")
  @Operation(summary = "Toggle destination availability")
  public ResponseEntity<Void> toggleDestinationAvailability(@PathVariable Long id) {
    destinationService.toggleDestinationAvailability(id);
    return ResponseEntity.ok().build();
  }
}
