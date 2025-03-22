package es.josetesan.ia.mcp.travelagency.service;

import es.josetesan.ia.mcp.travelagency.domain.Customer;
import es.josetesan.ia.mcp.travelagency.domain.Destination;
import es.josetesan.ia.mcp.travelagency.domain.TravelPackage;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiService {

  private final CustomerService customerService;
  private final DestinationService destinationService;
  private final TravelPackageService travelPackageService;

  public AiService(
      CustomerService customerService,
      DestinationService destinationService,
      TravelPackageService travelPackageService) {
    this.customerService = customerService;
    this.destinationService = destinationService;
    this.travelPackageService = travelPackageService;
  }

  @Tool(
      description =
          "Retrieves all the cities of a given country and the hotel price per day available")
  public List<Destination> getCitiesByCountry(
      @ToolParam(description = "Country to filter results") String country) {
    log.info("Called getDestinationsByCountry {}", country);
    var destinations = this.destinationService.getDestinationsByCountry(country);
    log.info("Found {} destinations for {}", destinations.size(), country);
    return destinations;
  }

  @Tool(description = "Creates a customer and returns its customer id to use later")
  public Customer createCustomer(
      @ToolParam(description = "Customer's firstname") String firstName,
      @ToolParam(description = "Customer's lastname") String lastname,
      @ToolParam(description = "Customer's email") String email) {
    var customer =
        this.customerService
            .getCustomerByEmail(email)
            .orElseGet(
                () -> {
                  log.info("Creating a customer by the email of {}", email);
                  var newCustomer =
                      Customer.builder()
                          .firstName(firstName)
                          .email(email)
                          .lastName(lastname)
                          .build();
                  try {
                    var created = this.customerService.createCustomer(newCustomer);
                    log.info("Created a customer {}", created);
                    return created;
                  } catch (Exception e) {
                    log.error("Error creating customer {}", email, e);
                    throw new RuntimeException("Error creating customer");
                  }
                });
    return customer;
  }

  @Tool(
      description =
          "Creates a travel package for a customer to a destination for a number of days with a given number of travellers")
  public TravelPackage createTravelPackage(
      @ToolParam(description = "The customer id") Long customerId,
      @ToolParam(description = "The destination id") Long destinationId,
      @ToolParam(description = "number of travellers") int numberOfTravellers,
      @ToolParam(description = "When to start the travel") LocalDate startDate,
      @ToolParam(description = "when to end the travel") LocalDate endDate) {
    try {
      ZoneId zoneId = ZoneId.of("Europe/Madrid");
      Customer customer = customerService.getCustomerById(customerId);
      log.info("Found customer {}", customer.getFirstName());
      Destination destination = destinationService.getDestinationById(destinationId);
      log.info(
          "Found destination {} with a price of {}",
          destination.getCity(),
          destination.getPricePerDay().doubleValue());
      log.info(
          "Creates a travelPackage with customer {}, destination {}, for {} travellers and starting {} , ending {}",
          customer,
          destination,
          numberOfTravellers,
          startDate,
          endDate);
      TravelPackage travelPackage =
          TravelPackage.builder()
              .customer(customer)
              .destination(destination)
              .numberOfTravelers(numberOfTravellers)
              .startDate(startDate.atStartOfDay(zoneId))
              .endDate(endDate.atStartOfDay(zoneId))
              .build();
      var created = this.travelPackageService.createTravelPackage(travelPackage);
      log.info("Created {}", created);
      return created;
    } catch (Exception e) {
      log.error("Error !!!", e);
      throw new RuntimeException("Could not create a travel package");
    }
  }
}
