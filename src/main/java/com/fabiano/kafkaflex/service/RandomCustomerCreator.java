package com.fabiano.kafkaflex.service;

import com.fabiano.kafkaflex.model.Address;
import com.fabiano.kafkaflex.model.Customer;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomCustomerCreator {
  private static final String NORMAL_PROCESSING_FLAG = "normal";

  private RandomCustomerCreator() {
    throw new IllegalStateException("Utility class");
  }

  public static Customer create(String processingFlag) {
    return Customer.builder()
        .name(RandomStringUtils.random(15, true, false))
        .email(RandomStringUtils
            .random(7, true, false)
            .concat("@")
            .concat("gmail.com"))
        .phone(RandomStringUtils.random(4, false, true)
            .concat("-")
            .concat(RandomStringUtils.random(6, false, true)))
        .address(Address.builder()
            .street(RandomStringUtils.random(10, true, false))
            .complement(RandomStringUtils.random(5, true, true))
            .number(RandomStringUtils.random(5, false, true))
            .zipCode(RandomStringUtils.random(2, false, true)
                .concat(".")
                .concat(RandomStringUtils.random(3, false, true)))
            .build())
        .country(CountryGenerator.generate())
        .processingFlag(processingFlag)
        .build();
  }

  public static Customer createWithValidationError() {
    Customer customer = RandomCustomerCreator.create(NORMAL_PROCESSING_FLAG);
    customer.setAddress(null);
    return customer;
  }
}
