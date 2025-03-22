package es.josetesan.ia.mcp.travelagency.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "travel_packages")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TravelPackage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  @NotNull(message = "Customer is required")
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "destination_id", nullable = false)
  @NotNull(message = "Destination is required")
  private Destination destination;

  @NotNull(message = "Start date is required")
  @Future(message = "Start date must be in the future")
  @Column(name = "start_date", nullable = false)
  private ZonedDateTime startDate;

  @NotNull(message = "End date is required")
  @Future(message = "End date must be in the future")
  @Column(name = "end_date", nullable = false)
  private ZonedDateTime endDate;

  @NotNull(message = "Number of travelers is required")
  @Positive(message = "Number of travelers must be positive")
  @Column(name = "number_of_travelers", nullable = false)
  private Integer numberOfTravelers;

  @NotNull(message = "Total price is required")
  @Positive(message = "Total price must be positive")
  @Column(name = "total_price", nullable = false)
  private BigDecimal totalPrice;

  @Column(name = "booking_status")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private BookingStatus status = BookingStatus.PENDING;

  public enum BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED
  }
}
