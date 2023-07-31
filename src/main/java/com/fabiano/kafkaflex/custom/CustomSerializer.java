package com.fabiano.kafkaflex.custom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.Serializable;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;


public class CustomSerializer<T extends Serializable> implements Serializer<T> {

  @Override
  public byte[] serialize(String topic, T data) {
    ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
        .json()
        .modulesToInstall(new JavaTimeModule())
        .build();
    try {
      if (data == null) {
        return new byte[0];
      }
      return objectMapper.writeValueAsBytes(data);
    } catch (JsonProcessingException e) {
      throw new SerializationException(String.format("Error when serializing %s from topic %s",
          data.getClass().getSimpleName(),
          topic));
    }
  }
}
