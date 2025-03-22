package es.josetesan.ia.mcp.travelagency.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "First name is required")
  @Column(name = "first_name", nullable = false)
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Email(message = "Email should be valid")
  @NotBlank(message = "Email is required")
  @Column(unique = true, nullable = false)
  private String email;
}
