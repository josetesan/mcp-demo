package es.josetesan.ia.mcp.travelagency.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "destinations")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Destination {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Country is required")
  @Column(nullable = false)
  private String country;

  @NotBlank(message = "City is required")
  @Column(nullable = false)
  private String city;

  @NotNull(message = "Price per day is required")
  @Positive(message = "Price per day must be positive")
  @Column(name = "price_per_day", nullable = false)
  private BigDecimal pricePerDay;

  @Column(name = "available")
  @Builder.Default
  private boolean available = true;
}
