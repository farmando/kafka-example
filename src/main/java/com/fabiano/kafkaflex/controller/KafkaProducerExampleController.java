package com.fabiano.kafkaflex.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.fabiano.kafkaflex.model.Customer;
import com.fabiano.kafkaflex.model.Transaction;
import com.fabiano.kafkaflex.service.RandomKafkaProducerService;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerExampleController implements KafkaExampleApi {

  private final RandomKafkaProducerService kafkaProducerService;

  public KafkaProducerExampleController(RandomKafkaProducerService kafkaProducerService) {
    this.kafkaProducerService = kafkaProducerService;
  }

  @Override
  public CompletableFuture<ResponseEntity<Transaction>> createNormalProcessingTransaction() {
    return kafkaProducerService.sendRandomNormalTransaction()
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(INTERNAL_SERVER_ERROR).build());
  }

  @Override
  public CompletableFuture<ResponseEntity<Transaction>> createRetryableTransaction() {
    return kafkaProducerService.sendRandomRetryableTransaction()
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(INTERNAL_SERVER_ERROR).build());
  }

  @Override
  public CompletableFuture<ResponseEntity<Transaction>> createDeadLetterTransaction() {
    return kafkaProducerService.sendTransactionToDeadLetter()
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(INTERNAL_SERVER_ERROR).build());
  }

  @Override
  public CompletableFuture<ResponseEntity<Customer>> createNormalProcessingCustomer() {
    return kafkaProducerService.sendRandomNormalProcessingCustomer()
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(INTERNAL_SERVER_ERROR).build());
  }

  @Override
  public CompletableFuture<ResponseEntity<Customer>> createRetryableCustomer() {
    return kafkaProducerService.sendRetryableCustomer()
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(INTERNAL_SERVER_ERROR).build());
  }

  @Override
  public CompletableFuture<ResponseEntity<Customer>> createCustomerDeadLetter() {
    return kafkaProducerService.sendRandomCustomerToDeadLetter()
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(INTERNAL_SERVER_ERROR).build());
  }
}
