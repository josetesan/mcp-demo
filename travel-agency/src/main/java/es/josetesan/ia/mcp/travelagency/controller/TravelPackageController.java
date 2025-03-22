package es.josetesan.ia.mcp.travelagency.controller;

import es.josetesan.ia.mcp.travelagency.domain.TravelPackage;
import es.josetesan.ia.mcp.travelagency.domain.TravelPackage.BookingStatus;
import es.josetesan.ia.mcp.travelagency.service.TravelPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/travel-packages")
@RequiredArgsConstructor
@Tag(name = "Travel Package", description = "Travel Package management APIs")
public class TravelPackageController {

  private final TravelPackageService travelPackageService;

  @GetMapping
  @Operation(summary = "Get all travel packages")
  public ResponseEntity<List<TravelPackage>> getAllTravelPackages() {
    return ResponseEntity.ok(travelPackageService.getAllTravelPackages());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get travel package by ID")
  public ResponseEntity<TravelPackage> getTravelPackageById(@PathVariable Long id) {
    return ResponseEntity.ok(travelPackageService.getTravelPackageById(id));
  }

  @GetMapping("/customer/{customerId}")
  @Operation(summary = "Get travel packages by customer ID")
  public ResponseEntity<List<TravelPackage>> getTravelPackagesByCustomer(
      @PathVariable Long customerId) {
    return ResponseEntity.ok(travelPackageService.getTravelPackagesByCustomer(customerId));
  }

  @GetMapping("/destination/{destinationId}")
  @Operation(summary = "Get travel packages by destination ID")
  public ResponseEntity<List<TravelPackage>> getTravelPackagesByDestination(
      @PathVariable Long destinationId) {
    return ResponseEntity.ok(travelPackageService.getTravelPackagesByDestination(destinationId));
  }

  @PostMapping
  @Operation(summary = "Create a new travel package")
  public ResponseEntity<TravelPackage> createTravelPackage(
      @Valid @RequestBody TravelPackage travelPackage) {
    TravelPackage createdPackage = travelPackageService.createTravelPackage(travelPackage);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdPackage);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing travel package")
  public ResponseEntity<TravelPackage> updateTravelPackage(
      @PathVariable Long id, @Valid @RequestBody TravelPackage travelPackage) {
    return ResponseEntity.ok(travelPackageService.updateTravelPackage(id, travelPackage));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a travel package")
  public ResponseEntity<Void> deleteTravelPackage(@PathVariable Long id) {
    travelPackageService.deleteTravelPackage(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/status")
  @Operation(summary = "Update travel package booking status")
  public ResponseEntity<TravelPackage> updateBookingStatus(
      @PathVariable Long id, @RequestParam BookingStatus status) {
    return ResponseEntity.ok(travelPackageService.updateBookingStatus(id, status));
  }
}
