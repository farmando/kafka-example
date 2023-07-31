package com.fabiano.kafkaflex.factory;

import com.fabiano.kafkaflex.config.KafkaConfig;
import com.fabiano.kafkaflex.custom.CustomDeserializer;
import com.fabiano.kafkaflex.model.Transaction;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class TransactionConsumerFactory extends KafkaConfig implements ConsumerFactoryCreator<Transaction> {

  @Autowired
  public TransactionConsumerFactory(KafkaProperties kafkaProperties) {
    super(kafkaProperties);
  }

  @Override
  public <K> ConsumerFactory<K, Transaction> createConsumerFactory(Map<String, Object> props) {
    DefaultKafkaConsumerFactory<K, Transaction> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
    consumerFactory.setValueDeserializer(new CustomDeserializer<>(Transaction.class));
    return consumerFactory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Transaction> transactionContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Transaction> containerFactory =
        new ConcurrentKafkaListenerContainerFactory<>();
    containerFactory.setConsumerFactory(createConsumerFactory(consumerConfigs()));
    return containerFactory;
  }
}