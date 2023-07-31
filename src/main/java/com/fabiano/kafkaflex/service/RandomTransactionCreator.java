package com.fabiano.kafkaflex.service;

import com.fabiano.kafkaflex.model.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class RandomTransactionCreator {
  private static final String NORMAL_PROCESSING_FLAG = "normal";

  private RandomTransactionCreator() {
    throw new IllegalStateException("Utility class");
  }

  public static Transaction create(String processingFlag) {
    return Transaction.builder()
        .amount(BigDecimal.valueOf(ThreadLocalRandom
                .current()
                .nextFloat() * (1000 - 15) + 15).setScale(2, RoundingMode.HALF_UP))
        .origin(CountryGenerator.generate())
        .destiny(CountryGenerator.generate())
        .date(LocalDateTime.now().toString())
        .processingFlag(processingFlag)
        .build();
  }

  public static Transaction createWithValidationError() {
    Transaction transaction = create(NORMAL_PROCESSING_FLAG);
    transaction.setOrigin(null);
    return transaction;
  }
}
