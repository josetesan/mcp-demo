package es.josetesan.ia.mcp.travelagency.controller;

import es.josetesan.ia.mcp.travelagency.domain.Customer;
import es.josetesan.ia.mcp.travelagency.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping
  @Operation(summary = "Get all customers")
  public ResponseEntity<List<Customer>> getAllCustomers() {
    return ResponseEntity.ok(customerService.getAllCustomers());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get customer by ID")
  public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
    return ResponseEntity.ok(customerService.getCustomerById(id));
  }

  @GetMapping("/email/{email}")
  @Operation(summary = "Get customer by email")
  public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
    var customer = customerService.getCustomerByEmail(email);
    return ResponseEntity.of(customer);
  }

  @PostMapping
  @Operation(summary = "Create a new customer")
  public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
    Customer createdCustomer = customerService.createCustomer(customer);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing customer")
  public ResponseEntity<Customer> updateCustomer(
      @PathVariable Long id, @Valid @RequestBody Customer customer) {
    return ResponseEntity.ok(customerService.updateCustomer(id, customer));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a customer")
  public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    customerService.deleteCustomer(id);
    return ResponseEntity.noContent().build();
  }
}
