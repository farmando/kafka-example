package com.fabiano.kafkaflex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fabiano.kafkaflex.model.Customer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RandomCustomerCreatorTest {
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void randomCustomerCreator_createValidCustomer() {
    String processingFlag = "normal";

    Customer customer = RandomCustomerCreator.create(processingFlag);
    Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

    assertTrue(violations.isEmpty(), "Customer should be valid");
  }

  @Test
  void randomCustomerCreator_createCustomerWithValidationError() {
    Customer customer = RandomCustomerCreator.createWithValidationError();

    Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

    assertFalse(violations.isEmpty(), "Customer should have validation errors");
    assertEquals(1, violations.size(), "There should be exactly one validation error");
  }

  @Test
  void randomCustomerCreator_customerWithInvalidProcessingFlagWontBeValid() {
    String invalidFlag = "invalidFlag";
    Customer customer = RandomCustomerCreator.create(invalidFlag);

    Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
    assertFalse(violations.isEmpty(), "Customer should have validation errors");
    assertEquals(1, violations.size(), "There should be exactly one validation error");
  }
}