package es.josetesan.ia.mcp.travelagency.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.josetesan.ia.mcp.travelagency.domain.Customer;
import es.josetesan.ia.mcp.travelagency.domain.Destination;
import es.josetesan.ia.mcp.travelagency.domain.TravelPackage;
import es.josetesan.ia.mcp.travelagency.domain.TravelPackage.BookingStatus;
import es.josetesan.ia.mcp.travelagency.repository.TravelPackageRepository;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
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
class TravelPackageServiceTest {

  @Mock private TravelPackageRepository travelPackageRepository;

  @Mock private CustomerService customerService;

  @Mock private DestinationService destinationService;

  @InjectMocks private TravelPackageService travelPackageService;

  private Customer customer;
  private Destination destination;
  private TravelPackage travelPackage;

  @BeforeEach
  void setUp() {
    customer =
        Customer.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .email("john@example.com")
            .build();

    destination =
        Destination.builder()
            .id(1L)
            .city("City")
            .pricePerDay(BigDecimal.valueOf(200))
            .available(true)
            .build();

    travelPackage =
        TravelPackage.builder()
            .id(1L)
            .customer(customer)
            .destination(destination)
            .startDate(ZonedDateTime.now())
            .endDate(ZonedDateTime.now().plusDays(5))
            .numberOfTravelers(2)
            .totalPrice(BigDecimal.valueOf(2000))
            .status(BookingStatus.PENDING)
            .build();
  }

  @Test
  void getAllTravelPackages_ShouldReturnListOfTravelPackages() {
    when(travelPackageRepository.findAll()).thenReturn(Collections.singletonList(travelPackage));

    List<TravelPackage> result = travelPackageService.getAllTravelPackages();

    assertThat(result).hasSize(1);
    assertThat(result.getFirst()).isEqualTo(travelPackage);
    verify(travelPackageRepository).findAll();
  }

  @Test
  void getTravelPackageById_WhenExists_ShouldReturnTravelPackage() {
    when(travelPackageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));

    TravelPackage result = travelPackageService.getTravelPackageById(1L);

    assertThat(result).isEqualTo(travelPackage);
    verify(travelPackageRepository).findById(1L);
  }

  @Test
  void createTravelPackage_WhenValidData_ShouldCreatePackage() {
    when(customerService.getCustomerById(1L)).thenReturn(customer);
    when(destinationService.getDestinationById(1L)).thenReturn(destination);
    when(travelPackageRepository.save(any(TravelPackage.class))).thenReturn(travelPackage);

    TravelPackage result = travelPackageService.createTravelPackage(travelPackage);

    assertThat(result).isEqualTo(travelPackage);
    verify(travelPackageRepository).save(any(TravelPackage.class));
  }

  @Test
  void createTravelPackage_WhenDestinationNotAvailable_ShouldThrowException() {
    destination.setAvailable(false);
    when(customerService.getCustomerById(1L)).thenReturn(customer);
    when(destinationService.getDestinationById(1L)).thenReturn(destination);

    assertThatThrownBy(() -> travelPackageService.createTravelPackage(travelPackage))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Selected destination is not available");
  }

  @Test
  void updateBookingStatus_WhenValidStatus_ShouldUpdateStatus() {
    when(travelPackageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));
    when(travelPackageRepository.save(any(TravelPackage.class))).thenReturn(travelPackage);

    TravelPackage result = travelPackageService.updateBookingStatus(1L, BookingStatus.CONFIRMED);

    assertThat(result.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
    verify(travelPackageRepository).save(any(TravelPackage.class));
  }

  @Test
  void deleteTravelPackage_WhenExists_ShouldDelete() {
    when(travelPackageRepository.existsById(1L)).thenReturn(true);

    travelPackageService.deleteTravelPackage(1L);

    verify(travelPackageRepository).deleteById(1L);
  }
}
