package com.fabiano.kafkaflex.service;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessingService {
  private final Logger logger = LoggerFactory.getLogger(MessageProcessingService.class);

  public void process(Retryable message) throws IOException {
    if (message.isRetryable()) {
      logger.info("retryable message found, {}", message);
      throw new IOException("ioexception");
    }
    logger.info("Message {} successfully processed", message);
  }
}
