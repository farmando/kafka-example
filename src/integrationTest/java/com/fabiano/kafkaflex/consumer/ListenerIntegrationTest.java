package com.fabiano.kafkaflex.consumer;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import com.fabiano.kafkaflex.consumer.config.InMemoryAppender;
import com.fabiano.kafkaflex.service.RandomKafkaProducerService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("integration")
@Testcontainers
class ListenerIntegrationTest {
  private static final String NORMAL_PROCESSING_FLAG = "normal";
  private static final String RETRY_PROCESSING_FLAG = "retry";
  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;
  private static final DockerImageName KAFKA_TEST_IMAGE =
      DockerImageName.parse("confluentinc/cp-kafka:7.2.1");
  private static final KafkaContainer kafka;
  private InMemoryAppender inMemoryAppender;

  @Autowired
  private RandomKafkaProducerService producerService;

  static {
    kafka = new KafkaContainer(KAFKA_TEST_IMAGE);
    kafka.start();
  }

  @BeforeEach
  void setup() throws JoranException {
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    loggerContext.reset();
    ContextInitializer initializer = new ContextInitializer(loggerContext);
    initializer.autoConfig();
    inMemoryAppender = new InMemoryAppender();
    inMemoryAppender.setContext(loggerContext);
    inMemoryAppender.start();
    ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
    rootLogger.addAppender(inMemoryAppender);
  }

  @DynamicPropertySource
  static void overridePropertiesInternal(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @Test
  void listener_canReadTransactionMessages() {
    bootstrapServers = kafka.getBootstrapServers();

    producerService.sendRandomNormalTransaction();

    await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
      List<String> logMessages = inMemoryAppender.getLogMessages()
          .stream()
          .filter(m -> m.contains("Received transaction message from topic kafka-example-transaction-message with"))
          .toList();;
      assertEquals(1, logMessages.size());
    });
  }

  @Test
  void listener_canReadCustomerMessages() {
    bootstrapServers = kafka.getBootstrapServers();

    producerService.sendRandomNormalProcessingCustomer();

    await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
      List<String> logMessages = inMemoryAppender.getLogMessages()
          .stream()
          .filter(m -> m.contains("Received customer message from topic kafka-example-customer-message with content"))
          .toList();;
      assertEquals(1, logMessages.size());
    });
  }

  @Test
  void listener_willSendToDeadLetterWhenReceivedMessageHasValidationErrorWithoutRetry() {
    bootstrapServers = kafka.getBootstrapServers();

    producerService.sendTransactionToDeadLetter();

    await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
      List<String> logMessages = inMemoryAppender.getLogMessages()
          .stream()
          .filter(m -> m.contains("Received message in dlt listener"))
          .toList();
      assertEquals(1, logMessages.size());
    });
  }

  @Test
  void listener_willRetryTheMessageThreeTimesBeforeSendToDeadLetter() {
    bootstrapServers = kafka.getBootstrapServers();

    producerService.sendRetryableCustomer();

    await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
      List<String> logMessages = inMemoryAppender.getLogMessages()
          .stream()
          .filter(m -> m.contains("Received customer message from topic kafka-example-customer-message"))
          .toList();
      assertTrue(logMessages.size() >= 3);
    });
  }
}
