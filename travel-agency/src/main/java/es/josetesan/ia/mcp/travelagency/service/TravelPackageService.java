package es.josetesan.ia.mcp.travelagency.service;

import es.josetesan.ia.mcp.travelagency.domain.Customer;
import es.josetesan.ia.mcp.travelagency.domain.Destination;
import es.josetesan.ia.mcp.travelagency.domain.TravelPackage;
import es.josetesan.ia.mcp.travelagency.domain.TravelPackage.BookingStatus;
import es.josetesan.ia.mcp.travelagency.repository.TravelPackageRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelPackageService {

  private final TravelPackageRepository travelPackageRepository;
  private final CustomerService customerService;
  private final DestinationService destinationService;

  public List<TravelPackage> getAllTravelPackages() {
    return travelPackageRepository.findAll();
  }

  public TravelPackage getTravelPackageById(Long id) {
    return travelPackageRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Travel package not found with id: " + id));
  }

  public List<TravelPackage> getTravelPackagesByCustomer(Long customerId) {
    Customer customer = customerService.getCustomerById(customerId);
    return travelPackageRepository.findByCustomer(customer);
  }

  public List<TravelPackage> getTravelPackagesByDestination(Long destinationId) {
    Destination destination = destinationService.getDestinationById(destinationId);
    return travelPackageRepository.findByDestination(destination);
  }

  @Transactional
  public TravelPackage createTravelPackage(TravelPackage travelPackage) {
    validateTravelPackage(travelPackage);
    calculateTotalPrice(travelPackage);
    travelPackage.setStatus(BookingStatus.PENDING);
    return travelPackageRepository.save(travelPackage);
  }

  @Transactional
  public TravelPackage updateTravelPackage(Long id, TravelPackage travelPackage) {
    TravelPackage existingPackage = getTravelPackageById(id);
    validateTravelPackage(travelPackage);

    existingPackage.setCustomer(travelPackage.getCustomer());
    existingPackage.setDestination(travelPackage.getDestination());
    existingPackage.setStartDate(travelPackage.getStartDate());
    existingPackage.setEndDate(travelPackage.getEndDate());
    existingPackage.setNumberOfTravelers(travelPackage.getNumberOfTravelers());

    calculateTotalPrice(existingPackage);
    return travelPackageRepository.save(existingPackage);
  }

  @Transactional
  public void deleteTravelPackage(Long id) {
    if (!travelPackageRepository.existsById(id)) {
      throw new EntityNotFoundException("Travel package not found with id: " + id);
    }
    travelPackageRepository.deleteById(id);
  }

  @Transactional
  public TravelPackage updateBookingStatus(Long id, BookingStatus status) {
    TravelPackage travelPackage = getTravelPackageById(id);
    travelPackage.setStatus(status);
    return travelPackageRepository.save(travelPackage);
  }

  private void validateTravelPackage(TravelPackage travelPackage) {
    if (travelPackage.getStartDate().isAfter(travelPackage.getEndDate())) {
      throw new IllegalArgumentException("Start date must be before end date");
    }
    if (!travelPackage.getDestination().isAvailable()) {
      throw new IllegalArgumentException("Selected destination is not available");
    }
    if (travelPackage.getStartDate().isBefore(ZonedDateTime.now())) {
      throw new IllegalArgumentException("Start date must be in the future");
    }
  }

  private void calculateTotalPrice(TravelPackage travelPackage) {
    long days =
        ChronoUnit.DAYS.between(travelPackage.getStartDate(), travelPackage.getEndDate()) + 1;
    BigDecimal pricePerDay = travelPackage.getDestination().getPricePerDay();
    BigDecimal totalPrice =
        pricePerDay
            .multiply(BigDecimal.valueOf(days))
            .multiply(BigDecimal.valueOf(travelPackage.getNumberOfTravelers()));
    travelPackage.setTotalPrice(totalPrice);
  }
}
