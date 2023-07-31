package com.fabiano.kafkaflex.producer;

import com.fabiano.kafkaflex.model.Customer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.kafka.core.KafkaTemplate;

public class CustomerProducer implements MessagePublisher<Customer> {
  private static final String CUSTOMER_TOPIC = "kafka-example-customer-message";
  private final KafkaTemplate<String, Customer> customerKafkaTemplate;
  private final Executor executor = Executors.newFixedThreadPool(10);

  public CustomerProducer(KafkaTemplate<String, Customer> customerKafkaTemplate) {
    this.customerKafkaTemplate = customerKafkaTemplate;
  }

  @Override
  public CompletableFuture<Void> publish(Customer customer) {
    return CompletableFuture.runAsync(() -> customerKafkaTemplate.send(CUSTOMER_TOPIC, customer), executor);
  }
}
