package com.fabiano.kafkaflex.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class CustomDeserializer<T extends Serializable> implements Deserializer<T> {
  private final Class<T> targetType;
  private final Logger logger = LoggerFactory.getLogger(CustomDeserializer.class);

  public CustomDeserializer(Class<T> targetType) {
    this.targetType = targetType;
  }

  @Override
  public T deserialize(String topic, byte[] data) {
    ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
        .json()
        .modulesToInstall(new JavaTimeModule())
        .build();
    try {
      if (data == null) {
        logger.info("Received NULL at deserializing");
        return null;
      }
      return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), targetType);
    } catch (IOException e) {
      logger.error("Error when deserializing to {}", targetType.getSimpleName());
      throw new SerializationException(String.format("Error when deserializing byte[] to %s",
          targetType.getSimpleName()), e);
    }
  }
}
