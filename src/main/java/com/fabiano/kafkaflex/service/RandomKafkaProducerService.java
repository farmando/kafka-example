package com.fabiano.kafkaflex.service;

import com.fabiano.kafkaflex.model.Customer;
import com.fabiano.kafkaflex.model.Transaction;
import com.fabiano.kafkaflex.producer.MessagePublisher;
import com.fabiano.kafkaflex.producer.MessagePublisherFactory;
import java.util.concurrent.CompletableFuture;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RandomKafkaProducerService {
  private static final String NORMAL_PROCESSING_FLAG = "normal";
  private static final String RETRY_PROCESSING_FLAG = "retry";

  private final MessagePublisher<Transaction> transactionPublisher;
  private final MessagePublisher<Customer> customerPublisher;


  public RandomKafkaProducerService(KafkaTemplate<String, Transaction> transactionKafkaTemplate,
                                    KafkaTemplate<String, Customer> customerKafkaTemplate) {
    transactionPublisher = MessagePublisherFactory.createTransactionProducer(transactionKafkaTemplate);
    customerPublisher = MessagePublisherFactory.createCustomerProducer(customerKafkaTemplate);
  }

  public CompletableFuture<Transaction> sendRandomNormalTransaction() {
    Transaction transaction = RandomTransactionCreator.create(NORMAL_PROCESSING_FLAG);
    return transactionPublisher.publish(transaction).thenApply(result -> transaction);
  }

  public CompletableFuture<Transaction> sendRandomRetryableTransaction() {
    Transaction transaction = RandomTransactionCreator.create(RETRY_PROCESSING_FLAG);
    return transactionPublisher.publish(transaction).thenApply(result -> transaction);
  }

  public CompletableFuture<Transaction> sendTransactionToDeadLetter() {
    Transaction transaction = RandomTransactionCreator.createWithValidationError();
    return transactionPublisher.publish(transaction).thenApply(result -> transaction);
  }

  public CompletableFuture<Customer> sendRandomNormalProcessingCustomer() {
    Customer customer = RandomCustomerCreator.create(NORMAL_PROCESSING_FLAG);
    return customerPublisher.publish(customer).thenApply(result -> customer);
  }

  public CompletableFuture<Customer> sendRetryableCustomer() {
    Customer customer = RandomCustomerCreator.create(RETRY_PROCESSING_FLAG);
    return customerPublisher.publish(customer).thenApply(result -> customer);
  }

  public CompletableFuture<Customer> sendRandomCustomerToDeadLetter() {
    Customer customer = RandomCustomerCreator.createWithValidationError();
    return customerPublisher.publish(customer).thenApply(result -> customer);
  }
}
