package com.fabiano.kafkaflex.factory;

import java.io.Serializable;
import java.util.Map;
import org.springframework.kafka.core.ConsumerFactory;

public interface ConsumerFactoryCreator<T extends Serializable> {
  <K> ConsumerFactory<K, T> createConsumerFactory(Map<String, Object> props);
}
