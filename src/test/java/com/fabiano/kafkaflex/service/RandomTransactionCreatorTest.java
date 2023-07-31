package com.fabiano.kafkaflex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fabiano.kafkaflex.model.Transaction;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RandomTransactionCreatorTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void randomTransactionCreator_createValidTransaction() {
    String processingFlag = "normal";

    Transaction transaction = RandomTransactionCreator.create(processingFlag);
    Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

    assertTrue(violations.isEmpty(), "Transaction should be valid");
  }


  @Test
  void randomTransactionCreator_createTransactionWithValidationError() {
    String processingFlag = "normal";

    Transaction transaction = RandomTransactionCreator.createWithValidationError();
    Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

    assertFalse(violations.isEmpty(), "Transaction should have validation errors");
    assertEquals(1, violations.size(), "There should be exactly one validation error");
  }

  @Test
  void randomTransactionCreator_transactionWithInvalidFlagWontBeValid() {
    String invalidFlag = "invalidFlag";

    Transaction transaction = RandomTransactionCreator.create(invalidFlag);
    Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

    assertFalse(violations.isEmpty(), "Transaction should have validation errors");
    assertEquals(1, violations.size(), "There should be exactly one validation error");
  }
}