package com.fabiano.kafkaflex.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fabiano.kafkaflex.model.Customer;
import com.fabiano.kafkaflex.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = {"/api/v1/producer"}, produces = APPLICATION_JSON_VALUE)
public interface KafkaExampleApi {
  @PostMapping(path = "/transaction", produces = APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Create transaction to be published to kafka",
      description = "Create a random transaction and publish it to kafka topic",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "The random transaction created and sent to kafka topic",
              content = {
                  @Content(
                      mediaType = APPLICATION_JSON_VALUE,
                      schema = @Schema(implementation = Transaction.class)
                  )
              }
          )
      }
  )
  CompletableFuture<ResponseEntity<Transaction>> createNormalProcessingTransaction();

  @PostMapping(path = "/retry/transaction", produces = APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Create retryable transaction to be published to kafka",
      description = "Create a random retryable transaction and publish it to kafka topic to be retrieved",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "The random transaction created and sent to kafka topic",
              content = {
                  @Content(
                      mediaType = APPLICATION_JSON_VALUE,
                      schema = @Schema(implementation = Transaction.class)
                  )
              }
          )
      }
  )
  CompletableFuture<ResponseEntity<Transaction>> createRetryableTransaction();

  @PostMapping(path = "/transaction-to-dead-letter", produces = APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Create transaction to be published to kafka deadLetter",
      description = "Create a invalid transaction and publish it to kafka topic that will end up to a deadLetter",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "The random transaction created and sent to deadLetter",
              content = {
                  @Content(
                      mediaType = APPLICATION_JSON_VALUE,
                      schema = @Schema(implementation = Transaction.class)
                  )
              }
          )
      }
  )
  CompletableFuture<ResponseEntity<Transaction>> createDeadLetterTransaction();

  @PostMapping(path = "/customer", produces = APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Create random customer to be published to kafka topic",
      description = "Create a random customer and publish it to kafka topic",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "The random customer created and sent to kafka topic",
              content = {
                  @Content(
                      mediaType = APPLICATION_JSON_VALUE,
                      schema = @Schema(implementation = Customer.class)
                  )
              }
          )
      }
  )
  CompletableFuture<ResponseEntity<Customer>> createNormalProcessingCustomer();

  @PostMapping(path = "/retry/customer", produces = APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Create random retryable customer to be published to kafka topic",
      description = "Create a random retryable customer and publish it to kafka topic",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "The random retryable customer created and sent to kafka topic",
              content = {
                  @Content(
                      mediaType = APPLICATION_JSON_VALUE,
                      schema = @Schema(implementation = Customer.class)
                  )
              }
          )
      }
  )
  CompletableFuture<ResponseEntity<Customer>> createRetryableCustomer();

  @PostMapping(path = "/customer-to-dead-letter", produces = APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Create random customer to be published to kafka deadLetter",
      description = "Create a invalid customer and publish it to kafka topic that will end up to a deadLetter",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "The random customer created and sent to kafka deadLetter",
              content = {
                  @Content(
                      mediaType = APPLICATION_JSON_VALUE,
                      schema = @Schema(implementation = Customer.class)
                  )
              }
          )
      }
  )
  CompletableFuture<ResponseEntity<Customer>> createCustomerDeadLetter();
}
