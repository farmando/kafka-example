package com.fabiano.kafkaflex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class MessageProcessingServiceTest {

  @Mock
  private Logger mockLogger;

  @InjectMocks
  private MessageProcessingService messageProcessingService;

  @Test
  void messageProcessingService_processNonRetryableMessage() throws IOException {
    // Set up the in-memory appender to capture log messages
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    Logger logger = loggerContext.getLogger(MessageProcessingService.class);
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    ((ch.qos.logback.classic.Logger) logger).addAppender(listAppender);

    Retryable mockMessage = Mockito.mock(Retryable.class);
    when(mockMessage.isRetryable()).thenReturn(false);
    messageProcessingService.process(mockMessage);

    List<ILoggingEvent> logEvents = listAppender.list;
    assertEquals(1, logEvents.size());
    ILoggingEvent logEvent = logEvents.get(0);
    assertEquals(Level.INFO, logEvent.getLevel());
    assertEquals("Message {} successfully processed", logEvent.getMessage());
    assertEquals(mockMessage, logEvent.getArgumentArray()[0]);

    ((ch.qos.logback.classic.Logger) logger).detachAppender(listAppender);
    listAppender.stop();
  }

  @Test
  void messageProcessingService_processRetryableMessage() {
    Retryable mockMessage = Mockito.mock(Retryable.class);
    when(mockMessage.isRetryable()).thenReturn(true);

    IOException thrown = Assertions.assertThrows(IOException.class, () -> {
      messageProcessingService.process(mockMessage);
    });

    assertEquals("ioexception", thrown.getMessage());
  }
}