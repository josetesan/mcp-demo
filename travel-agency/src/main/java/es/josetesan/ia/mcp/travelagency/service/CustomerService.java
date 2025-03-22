package es.josetesan.ia.mcp.travelagency.service;

import es.josetesan.ia.mcp.travelagency.domain.Customer;
import es.josetesan.ia.mcp.travelagency.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

  private final CustomerRepository customerRepository;

  public List<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

  public Customer getCustomerById(Long id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
  }

  public Optional<Customer> getCustomerByEmail(String email) {
    return customerRepository.findByEmail(email);
  }

  @Transactional
  public Customer createCustomer(Customer customer) {
    if (customerRepository.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException(
          "Customer already exists with email: " + customer.getEmail());
    }
    return customerRepository.save(customer);
  }

  @Transactional
  public Customer updateCustomer(Long id, Customer customer) {
    Customer existingCustomer = getCustomerById(id);

    if (!existingCustomer.getEmail().equals(customer.getEmail())
        && customerRepository.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("Email already in use: " + customer.getEmail());
    }

    existingCustomer.setFirstName(customer.getFirstName());
    existingCustomer.setLastName(customer.getLastName());
    existingCustomer.setEmail(customer.getEmail());

    return customerRepository.save(existingCustomer);
  }

  @Transactional
  public void deleteCustomer(Long id) {
    if (!customerRepository.existsById(id)) {
      throw new EntityNotFoundException("Customer not found with id: " + id);
    }
    customerRepository.deleteById(id);
  }
}
