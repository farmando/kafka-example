package com.fabiano.kafkaflex.producer;

import com.fabiano.kafkaflex.model.Transaction;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TransactionProducer implements MessagePublisher<Transaction> {
  private static final String TRANSACTION_TOPIC = "kafka-example-transaction-message";
  private final KafkaTemplate<String, Transaction> transactionKafkaTemplate;
  private final Executor executor = Executors.newFixedThreadPool(10);

  public TransactionProducer(KafkaTemplate<String, Transaction> transactionKafkaTemplate) {
    this.transactionKafkaTemplate = transactionKafkaTemplate;
  }

  @Override
  public CompletableFuture<Void> publish(Transaction transaction) {
    return CompletableFuture.runAsync(() -> transactionKafkaTemplate.send(TRANSACTION_TOPIC, transaction), executor);
  }
}
