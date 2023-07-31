package com.fabiano.kafkaflex.producer;

import java.util.concurrent.CompletableFuture;

public interface MessagePublisher<T> {
  CompletableFuture<Void> publish(T message);
}
