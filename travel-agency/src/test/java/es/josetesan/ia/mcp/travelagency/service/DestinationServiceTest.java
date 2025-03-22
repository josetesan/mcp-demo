package es.josetesan.ia.mcp.travelagency.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.josetesan.ia.mcp.travelagency.domain.Destination;
import es.josetesan.ia.mcp.travelagency.repository.DestinationRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DestinationServiceTest {

  @Mock private DestinationRepository destinationRepository;

  @InjectMocks private DestinationService destinationService;

  private Destination destination;

  @BeforeEach
  void setUp() {
    destination =
        Destination.builder()
            .id(1L)
            .country("France")
            .city("Paris")
            .pricePerDay(BigDecimal.valueOf(200))
            .available(true)
            .build();
  }

  @Test
  void getAllDestinations_ShouldReturnListOfDestinations() {
    when(destinationRepository.findAll()).thenReturn(Collections.singletonList(destination));

    List<Destination> result = destinationService.getAllDestinations();

    assertThat(result).hasSize(1);
    assertThat(result.getFirst()).isEqualTo(destination);
    verify(destinationRepository).findAll();
  }

  @Test
  void getAvailableDestinations_ShouldReturnAvailableDestinations() {
    when(destinationRepository.findByAvailableTrue())
        .thenReturn(Collections.singletonList(destination));

    List<Destination> result = destinationService.getAvailableDestinations();

    assertThat(result).hasSize(1);
    assertThat(result.getFirst()).isEqualTo(destination);
    verify(destinationRepository).findByAvailableTrue();
  }

  @Test
  void getDestinationById_WhenDestinationExists_ShouldReturnDestination() {
    when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));

    Destination result = destinationService.getDestinationById(1L);

    assertThat(result).isEqualTo(destination);
    verify(destinationRepository).findById(1L);
  }

  @Test
  void getDestinationById_WhenDestinationDoesNotExist_ShouldThrowException() {
    when(destinationRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> destinationService.getDestinationById(1L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Destination not found with id: 1");
  }

  @Test
  void createDestination_ShouldCreateDestination() {
    when(destinationRepository.save(any(Destination.class))).thenReturn(destination);

    Destination result = destinationService.createDestination(destination);

    assertThat(result).isEqualTo(destination);
    verify(destinationRepository).save(destination);
  }

  @Test
  void updateDestination_WhenDestinationExists_ShouldUpdateDestination() {
    Destination updatedDestination =
        Destination.builder()
            .city("Updated Paris Adventure")
            .pricePerDay(BigDecimal.valueOf(250))
            .build();

    when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));
    when(destinationRepository.save(any(Destination.class))).thenReturn(updatedDestination);

    Destination result = destinationService.updateDestination(1L, updatedDestination);

    assertThat(result).isEqualTo(updatedDestination);
    verify(destinationRepository).findById(1L);
    verify(destinationRepository).save(any(Destination.class));
  }

  @Test
  void deleteDestination_WhenDestinationExists_ShouldDeleteDestination() {
    when(destinationRepository.existsById(1L)).thenReturn(true);

    destinationService.deleteDestination(1L);

    verify(destinationRepository).existsById(1L);
    verify(destinationRepository).deleteById(1L);
  }

  @Test
  void toggleDestinationAvailability_WhenDestinationExists_ShouldToggleAvailability() {
    when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));
    when(destinationRepository.save(any(Destination.class))).thenReturn(destination);

    destinationService.toggleDestinationAvailability(1L);

    verify(destinationRepository).findById(1L);
    verify(destinationRepository).save(any(Destination.class));
    assertThat(destination.isAvailable()).isFalse();
  }
}
