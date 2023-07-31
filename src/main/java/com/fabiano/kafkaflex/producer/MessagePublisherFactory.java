package com.fabiano.kafkaflex.producer;

import com.fabiano.kafkaflex.model.Customer;
import com.fabiano.kafkaflex.model.Transaction;
import org.springframework.kafka.core.KafkaTemplate;

public class MessagePublisherFactory {
  private MessagePublisherFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static MessagePublisher<Transaction> createTransactionProducer(KafkaTemplate<String, Transaction> template) {
    return new TransactionProducer(template);
  }

  public static MessagePublisher<Customer> createCustomerProducer(KafkaTemplate<String, Customer> template) {
    return new CustomerProducer(template);
  }
}
