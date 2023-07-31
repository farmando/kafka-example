package com.fabiano.kafkaflex.consumer;

import com.fabiano.kafkaflex.model.Customer;
import com.fabiano.kafkaflex.model.Transaction;
import com.fabiano.kafkaflex.service.MessageProcessingService;
import java.io.IOException;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@EnableKafka
@Component
public class Listener {
  private final Logger logger = LoggerFactory.getLogger(Listener.class);
  private final MessageProcessingService messageProcessingService;

  public Listener(MessageProcessingService messageProcessingService) {
    this.messageProcessingService = messageProcessingService;
  }

  @RetryableTopic(attempts = "3",
          backoff = @Backoff(delay = 1000, multiplier = 3.0),
          autoCreateTopics = "false ",
          topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
          dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR,
          exclude = SerializationException.class,
          listenerContainerFactory = "transactionContainerFactory"
  )
  @KafkaListener(topics = "kafka-example-transaction-message",
          groupId = "group-id-1",
          containerFactory = "transactionContainerFactory")
  public void listenTransaction(@Payload @Validated Transaction message,
                                @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws IOException {
    logger.info("Received transaction message from topic {} with content {}", topic, message);
    messageProcessingService.process(message);
  }

  @RetryableTopic(attempts = "3",
          backoff = @Backoff(delay = 1000, multiplier = 3.0),
          autoCreateTopics = "false",
          topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
          dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR,
          exclude = SerializationException.class,
          listenerContainerFactory = "customerContainerFactory"
  )
  @KafkaListener(topics = "kafka-example-customer-message",
      groupId = "group-id-1",
      containerFactory = "customerContainerFactory")
  public void listenCustomer(@Payload @Validated Customer message,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws IOException {
    logger.info("Received customer message from topic {} with content {}", topic, message);
    messageProcessingService.process(message);
  }
}
