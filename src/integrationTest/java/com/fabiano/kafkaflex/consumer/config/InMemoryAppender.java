package com.fabiano.kafkaflex.consumer.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.ArrayList;
import java.util.List;

public class InMemoryAppender extends AppenderBase<ILoggingEvent> {
  private final List<String> logMessages = new ArrayList<>();

  @Override
  protected void append(ILoggingEvent eventObject) {
    if (eventObject.getLevel().isGreaterOrEqual(ch.qos.logback.classic.Level.INFO)) {
      logMessages.add(eventObject.getFormattedMessage());
    }
  }

  public List<String> getLogMessages() {
    return logMessages;
  }
}
