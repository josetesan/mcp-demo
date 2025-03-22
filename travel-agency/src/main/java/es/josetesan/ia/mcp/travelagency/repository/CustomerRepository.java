package es.josetesan.ia.mcp.travelagency.repository;

import es.josetesan.ia.mcp.travelagency.domain.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByEmail(String email);

  boolean existsByEmail(String email);
}
