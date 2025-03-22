package es.josetesan.ia.mcp.travelagency.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.josetesan.ia.mcp.travelagency.domain.Customer;
import es.josetesan.ia.mcp.travelagency.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
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
class CustomerServiceTest {

  @Mock private CustomerRepository customerRepository;

  @InjectMocks private CustomerService customerService;

  private Customer customer;

  @BeforeEach
  void setUp() {
    customer =
        Customer.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();
  }

  @Test
  void getAllCustomers_ShouldReturnListOfCustomers() {
    when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));

    List<Customer> result = customerService.getAllCustomers();

    assertThat(result).hasSize(1);
    assertThat(result.getFirst()).isEqualTo(customer);
    verify(customerRepository).findAll();
  }

  @Test
  void getCustomerById_WhenCustomerExists_ShouldReturnCustomer() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

    Customer result = customerService.getCustomerById(1L);

    assertThat(result).isEqualTo(customer);
    verify(customerRepository).findById(1L);
  }

  @Test
  void getCustomerById_WhenCustomerDoesNotExist_ShouldThrowException() {
    when(customerRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> customerService.getCustomerById(1L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Customer not found with id: 1");
  }

  @Test
  void createCustomer_WhenEmailNotTaken_ShouldCreateCustomer() {
    when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);
    when(customerRepository.save(any(Customer.class))).thenReturn(customer);

    Customer result = customerService.createCustomer(customer);

    assertThat(result).isEqualTo(customer);
    verify(customerRepository).existsByEmail(customer.getEmail());
    verify(customerRepository).save(customer);
  }

  @Test
  void createCustomer_WhenEmailTaken_ShouldThrowException() {
    when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> customerService.createCustomer(customer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Customer already exists with email");
  }

  @Test
  void updateCustomer_WhenCustomerExists_ShouldUpdateCustomer() {

    Customer updatedCustomer = Customer.builder().firstName("Jane").lastName("Smith").build();

    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

    Customer result = customerService.updateCustomer(1L, updatedCustomer);

    assertThat(result).isEqualTo(updatedCustomer);
    verify(customerRepository).findById(1L);
    verify(customerRepository).save(any(Customer.class));
  }

  @Test
  void deleteCustomer_WhenCustomerExists_ShouldDeleteCustomer() {
    when(customerRepository.existsById(1L)).thenReturn(true);

    customerService.deleteCustomer(1L);

    verify(customerRepository).existsById(1L);
    verify(customerRepository).deleteById(1L);
  }

  @Test
  void deleteCustomer_WhenCustomerDoesNotExist_ShouldThrowException() {
    when(customerRepository.existsById(1L)).thenReturn(false);

    assertThatThrownBy(() -> customerService.deleteCustomer(1L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Customer not found with id: 1");
  }
}
