package com.fabiano.kafkaflex.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.fabiano.kafkaflex.model.Address;
import com.fabiano.kafkaflex.model.Customer;
import com.fabiano.kafkaflex.model.Transaction;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

class RandomKafkaProducerServiceTest {
  @Mock
  private KafkaTemplate<String, Transaction> transactionKafkaTemplate;
  @Mock
  private KafkaTemplate<String, Customer> customerKafkaTemplate;
  @InjectMocks
  private RandomKafkaProducerService producerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    producerService = new RandomKafkaProducerService(transactionKafkaTemplate, customerKafkaTemplate);
  }

  @AfterEach
  void tearDown() {
    try (MockedStatic<RandomTransactionCreator> transactionCreatorMock = mockStatic(RandomTransactionCreator.class);
         MockedStatic<RandomCustomerCreator> customerCreatorMock = mockStatic(RandomCustomerCreator.class)) {
      ;
    }

  }

  @Test
  void randomKafkaProducerService_sendRandomNormalTransaction() {

    try (MockedStatic<RandomTransactionCreator> transactionCreatorMock = mockStatic(RandomTransactionCreator.class)) {
      Transaction mockTransaction = Transaction.builder()
          .id(1)
          .amount(BigDecimal.valueOf(100.0))
          .origin("Origin")
          .destiny("Destiny")
          .date(String.valueOf(Instant.now()))
          .processingFlag("normal")
          .build();

      //mockStatic(RandomTransactionCreator.class);
      when(RandomTransactionCreator.create(anyString()))
          .thenReturn(mockTransaction);

      CompletableFuture<Transaction> resultFuture = producerService.sendRandomNormalTransaction();
      Transaction resultTransaction = resultFuture.join();

      assertNotNull(resultTransaction);
      assertSame(mockTransaction, resultTransaction);
    }
  }

  @Test
  void randomKafkaProducerService_sendRandomRetryableTransaction() {
    try (MockedStatic<RandomTransactionCreator> transactionCreatorMock = mockStatic(RandomTransactionCreator.class)) {
      Transaction mockTransaction = Transaction.builder()
          .id(1)
          .amount(BigDecimal.valueOf(100.0))
          .origin("Origin")
          .destiny("Destiny")
          .date(String.valueOf(Instant.now()))
          .processingFlag("normal")
          .build();

      when(RandomTransactionCreator.create("retry"))
          .thenReturn(mockTransaction);

      CompletableFuture<Transaction> resultFuture = producerService.sendRandomRetryableTransaction();
      Transaction resultTransaction = resultFuture.join();

      assertNotNull(resultTransaction);
      assertSame(mockTransaction, resultTransaction);
    }
  }

  @Test
  void randomKafkaProducerService_sendTransactionToDeadLetter() {
    try (MockedStatic<RandomTransactionCreator> transactionCreatorMock = mockStatic(RandomTransactionCreator.class)) {
      Transaction mockTransaction = Transaction.builder()
          .id(1)
          .amount(BigDecimal.valueOf(100.0))
          .origin(null)
          .destiny("Destiny")
          .date(String.valueOf(Instant.now()))
          .processingFlag("normal")
          .build();

      when(RandomTransactionCreator.createWithValidationError())
          .thenReturn(mockTransaction);

      CompletableFuture<Transaction> resultFuture = producerService.sendTransactionToDeadLetter();
      Transaction resultTransaction = resultFuture.join();

      assertNotNull(resultTransaction);
      assertSame(mockTransaction, resultTransaction);
    }
  }

  @Test
  void randomKafkaProducerService_sendRandomNormalProcessingCustomer() {
    try (MockedStatic<RandomCustomerCreator> customerCreatorMock = mockStatic(RandomCustomerCreator.class)) {
      Customer mockCustomer = Customer.builder()
          .name("name")
          .phone("1234")
          .email("mail@domain.com")
          .country("United States")
          .address(Address.builder()
              .street("street")
              .number("123")
              .zipCode("12.345-000")
              .build())
          .processingFlag("normal")
          .build();

      when(RandomCustomerCreator.create(any()))
          .thenReturn(mockCustomer);

      CompletableFuture<Customer> resultFuture = producerService.sendRandomNormalProcessingCustomer();
      Customer resultCustomer = resultFuture.join();

      assertNotNull(resultCustomer);
      assertSame(mockCustomer, resultCustomer);
    }
  }

  @Test
  void randomKafkaProducerService_sendRetryableCustomer() {
    try (MockedStatic<RandomCustomerCreator> customerCreatorMock = mockStatic(RandomCustomerCreator.class)) {
      Customer mockCustomer = Customer.builder()
          .name("name")
          .phone("1234")
          .email("mail@domain.com")
          .country("United States")
          .address(Address.builder()
              .street("street")
              .number("123")
              .zipCode("12.345-000")
              .build())
          .processingFlag("retry")
          .build();

      when(RandomCustomerCreator.create(any()))
          .thenReturn(mockCustomer);

      CompletableFuture<Customer> resultFuture = producerService.sendRetryableCustomer();
      Customer resultCustomer = resultFuture.join();

      assertNotNull(resultCustomer);
      assertSame(mockCustomer, resultCustomer);
    }
  }

  @Test
  void randomKafkaProducerService_sendRandomCustomerToDeadLetter() {
    try (MockedStatic<RandomCustomerCreator> customerCreatorMock = mockStatic(RandomCustomerCreator.class)) {
      Customer mockCustomer = Customer.builder()
          .name("name")
          .phone("1234")
          .email("mail@domain.com")
          .country("United States")
          .address(Address.builder()
              .street("street")
              .number("123")
              .zipCode("12.345-000")
              .build())
          .processingFlag("normal")
          .build();

      when(RandomCustomerCreator.createWithValidationError())
          .thenReturn(mockCustomer);

      CompletableFuture<Customer> resultFuture = producerService.sendRandomCustomerToDeadLetter();
      Customer resultCustomer = resultFuture.join();

      assertNotNull(resultCustomer);
      assertSame(mockCustomer, resultCustomer);
    }
  }
}
