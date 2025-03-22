package es.josetesan.ia.mcp.travelagency.repository;

import es.josetesan.ia.mcp.travelagency.domain.Customer;
import es.josetesan.ia.mcp.travelagency.domain.Destination;
import es.josetesan.ia.mcp.travelagency.domain.TravelPackage;
import es.josetesan.ia.mcp.travelagency.domain.TravelPackage.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {
  List<TravelPackage> findByCustomer(Customer customer);

  List<TravelPackage> findByDestination(Destination destination);

  List<TravelPackage> findByStatus(BookingStatus status);

  List<TravelPackage> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

  List<TravelPackage> findByCustomerAndStatus(Customer customer, BookingStatus status);
}
