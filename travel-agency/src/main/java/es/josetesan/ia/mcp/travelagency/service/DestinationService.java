package es.josetesan.ia.mcp.travelagency.service;

import es.josetesan.ia.mcp.travelagency.domain.Destination;
import es.josetesan.ia.mcp.travelagency.repository.DestinationRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DestinationService {

  private final DestinationRepository destinationRepository;

  public List<Destination> getAllDestinations() {
    return destinationRepository.findAll();
  }

  public List<Destination> getAvailableDestinations() {
    return destinationRepository.findByAvailableTrue();
  }

  public Destination getDestinationById(Long id) {
    return destinationRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + id));
  }

  public List<Destination> getDestinationsByCountry(String country) {
    return destinationRepository.findByCountryIgnoreCase(country);
  }

  public List<Destination> getDestinationsByCity(String city) {
    return destinationRepository.findByCityIgnoreCase(city);
  }

  @Transactional
  public Destination createDestination(Destination destination) {
    return destinationRepository.save(destination);
  }

  @Transactional
  public Destination updateDestination(Long id, Destination destination) {
    Destination existingDestination = getDestinationById(id);

    existingDestination.setCountry(destination.getCountry());
    existingDestination.setCity(destination.getCity());
    existingDestination.setPricePerDay(destination.getPricePerDay());
    existingDestination.setAvailable(destination.isAvailable());

    return destinationRepository.save(existingDestination);
  }

  @Transactional
  public void deleteDestination(Long id) {
    if (!destinationRepository.existsById(id)) {
      throw new EntityNotFoundException("Destination not found with id: " + id);
    }
    destinationRepository.deleteById(id);
  }

  @Transactional
  public void toggleDestinationAvailability(Long id) {
    Destination destination = getDestinationById(id);
    destination.setAvailable(!destination.isAvailable());
    destinationRepository.save(destination);
  }
}
