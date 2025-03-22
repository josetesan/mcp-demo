package es.josetesan.ia.mcp.travelagency.repository;

import es.josetesan.ia.mcp.travelagency.domain.Destination;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
  List<Destination> findByCountryIgnoreCase(String country);

  List<Destination> findByCityIgnoreCase(String city);

  List<Destination> findByAvailableTrue();

  List<Destination> findByCountryAndCityIgnoreCase(String country, String city);
}
