package com.fabiano.kafkaflex.service;

import io.swagger.v3.oas.annotations.media.Schema;

public interface Retryable {
  @Schema(hidden = true)
  boolean isRetryable();
}
